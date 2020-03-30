package com.edou.blog.repository.es;

import com.edou.blog.domain.es.EsBlog;
import com.sun.deploy.panel.JreFindDialog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
/*
 * @Description //用于检索blog的持久层接口
 * @Date 2020/3/26 18:03ES
 * @param null
 *@return
 **/
public interface EsBlogRepository extends ElasticsearchRepository<EsBlog,String> {
    //根据blogId查询
    EsBlog findByBlogId(Long blogId);

    /**
     * 模糊查询(去重)
     * @param title
     * @param summary
     * @param content
     * @param tags
     * @param pageable
     * @return
     */
    Page<EsBlog> findDistinctByTitleContainingOrSummaryContainingOrContentContainingOrTagsContaining(String title, String summary, String content, String tags, Pageable pageable);
}
