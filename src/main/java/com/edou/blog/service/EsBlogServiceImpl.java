package com.edou.blog.service;

import com.edou.blog.domain.User;
import com.edou.blog.domain.es.EsBlog;
import com.edou.blog.repository.es.EsBlogRepository;
import com.edou.blog.vo.TagVO;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName EsBlogServiceImpl
 * @Description EsBlogService的实现类
 * @Author 中森明菜
 * @Date 2020/3/26 18:14
 * @Version 1.0
 */
@Service
public class EsBlogServiceImpl implements EsBlogService{
    @Autowired
    private EsBlogRepository esBlogRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private UserService userService;

//    private static final Pageable TOP_5_PAGEABLE = new PageRequest(0, 5);
    private static final Pageable TOP_5_PAGEABLE = PageRequest.of(0,5);
    private static final String EMPTY_KEYWORD = "";

    @Override
    public void removeEsBlog(String id) {
        esBlogRepository.deleteById(id);
//        esBlogRepository.delete(id);
    }

    @Override
    public EsBlog updateEsBlog(EsBlog esBlog) {
        EsBlog blog = esBlogRepository.save(esBlog);
        return blog;
    }

    @Override
    public EsBlog getEsBlogByBlogId(Long blogId) {
        return esBlogRepository.findByBlogId(blogId);
    }

    //最新查询 可输入关键字
    @Override
    public Page<EsBlog> listNewestEsBlogs(String keyword, Pageable pageable) {
        Page<EsBlog> page = null;
        Sort sort = new Sort(Sort.Direction.DESC,"createTime");
        if(pageable.getSort().toString().equals("UNSORTED")){
            pageable = PageRequest.of(pageable.getPageNumber(),pageable.getPageSize(),sort);
//            pageable = new PageRequest(pageable.getPageNumber(),pageable.getPageSize(),sort);
        }
        page = esBlogRepository.findDistinctByTitleContainingOrSummaryContainingOrContentContainingOrTagsContaining(keyword,keyword,keyword,keyword,pageable);
        return page;
    }

    //最热查询
    @Override
    public Page<EsBlog> listHotestEsBlogs(String keyword, Pageable pageable) {
        Page<EsBlog> page = null;
        Sort sort = new Sort(Sort.Direction.DESC,"readSize","commentSize","voteSize","createTime");
        if(pageable.getSort().toString().equals("UNSORTED")){
            pageable = PageRequest.of(pageable.getPageNumber(),pageable.getPageSize(),sort);
//            pageable = new PageRequest(pageable.getPageNumber(),pageable.getPageSize(),sort);
        }
        return esBlogRepository.findDistinctByTitleContainingOrSummaryContainingOrContentContainingOrTagsContaining(keyword,keyword,keyword,keyword,pageable);
    }

    @Override
    public Page<EsBlog> listEsBlogs(Pageable pageable) {
        return esBlogRepository.findAll(pageable);
    }

    //最新前5
    @Override
    public List<EsBlog> listTop5NewestEsBlogs() {
        Page<EsBlog> esBlogs = this.listNewestEsBlogs(EMPTY_KEYWORD, TOP_5_PAGEABLE);
        return esBlogs.getContent();
    }

    //最热前5
    @Override
    public List<EsBlog> listTop5HotestEsBlogs() {
        Page<EsBlog> esBlogs = this.listHotestEsBlogs(EMPTY_KEYWORD, TOP_5_PAGEABLE);
        return esBlogs.getContent();
    }

    //热门标签数量前30
    @Override
    public List<TagVO> listTop30Tags() {
        List<TagVO> list = new ArrayList<>();
        //构造查询条件
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchAllQuery())
                .withIndices("blog").withTypes("blog")
                .addAggregation(AggregationBuilders.terms("tags").field("tags.keyword").order(Terms.Order.count(false)).size(30))
                .build();
        //执行查询
        Aggregations aggregations = elasticsearchTemplate.query(searchQuery, new ResultsExtractor<Aggregations>() {
            @Override
            public Aggregations extract(SearchResponse response) {
                return response.getAggregations();
            }
        });
        StringTerms stringTerms = (StringTerms)aggregations.asMap().get("tags");
        Iterator<StringTerms.Bucket> iterator = stringTerms.getBuckets().iterator();
//        Iterator<Terms.Bucket> iterator = stringTerms.getBuckets().iterator();
        while(iterator.hasNext()){
            Terms.Bucket bucket = iterator.next();
            list.add(new TagVO(bucket.getKey().toString(),bucket.getDocCount()));
        }
        return list;
    }

    //热门用户前12位
    @Override
    public List<User> listTop12Users() {
        List<String> list = new ArrayList<>();
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(QueryBuilders.matchAllQuery())
                .withIndices("blog").withTypes("blog")
                .withSearchType(SearchType.QUERY_THEN_FETCH)
                .addAggregation(AggregationBuilders.terms("users").field("username.keyword").order(Terms.Order.count(false)).size(12))
                .build();
        Aggregations aggregations = elasticsearchTemplate.query(searchQuery, new ResultsExtractor<Aggregations>() {
            @Override
            public Aggregations extract(SearchResponse response) {
                return response.getAggregations();
            }
        });
        StringTerms stringTerms = (StringTerms)aggregations.asMap().get("users");
        Iterator<StringTerms.Bucket> bucketIterator = stringTerms.getBuckets().iterator();
//        Iterator<Terms.Bucket> bucketIterator = stringTerms.getBuckets().iterator();
        while (bucketIterator.hasNext()){
            Terms.Bucket bucket = bucketIterator.next();
            list.add(bucket.getKey().toString());
        }
        List<User> users = userService.listUsersByUsernames(list);
        return users;
    }
}
