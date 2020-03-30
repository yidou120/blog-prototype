package com.edou.blog.domain.es;

import com.edou.blog.domain.Blog;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @ClassName EsBlog
 * @Description 用于全文检索的Blog实体
 * @Author 中森明菜
 * @Date 2020/3/26 17:26
 * @Version 1.0
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(indexName = "blog",type = "blog",shards = 1,replicas = 0)
public class EsBlog implements Serializable {

    private static final long serialVersionUID = -3039404347950817231L;

    @Id
    private String id;

//    @Field(index = FieldIndex.not_analyzed)
    @Field(index = false,type = FieldType.keyword)
    private Long blogId;

//    @Field(analyzer = "ik_smart",searchAnalyzer = "ik_smart",type = FieldType.String)
    @Field(analyzer = "ik_smart",searchAnalyzer = "ik_smart",type = FieldType.text)
    private String title;

//    @Field(store = true,analyzer = "ik_smart",searchAnalyzer = "ik_smart",type = FieldType.String)
    @Field(analyzer = "ik_smart",searchAnalyzer = "ik_smart",type = FieldType.text)
    private String summary;

//    @Field(store = true,analyzer = "ik_smart",searchAnalyzer = "ik_smart",type = FieldType.String)
    @Field(analyzer = "ik_smart",searchAnalyzer = "ik_smart",type = FieldType.text)
    private String content;

//    @Field(index = FieldIndex.not_analyzed,type = FieldType.String)
    @Field(type = FieldType.keyword)
    private String username;

//    @Field(index = FieldIndex.not_analyzed,type = FieldType.String)
    @Field(index = false,type = FieldType.keyword)
    private String avatar;

//    @Field(index = FieldIndex.not_analyzed,type = FieldType.String)
    @Field(index = false,type = FieldType.Long)
    private Timestamp createTime;

//    @Field(index = FieldIndex.not_analyzed,type = FieldType.String)
    @Field(type = FieldType.Integer)
    private Integer readSize = 0; // 访问量、阅读量

//    @Field(index = FieldIndex.not_analyzed,type = FieldType.String)
    @Field(type = FieldType.Integer)
    private Integer commentSize = 0;  // 评论量

//    @Field(index = FieldIndex.not_analyzed,type = FieldType.String)
    @Field(type = FieldType.Integer)
    private Integer voteSize = 0;  // 点赞量

//    @Field(analyzer = "ik_smart",searchAnalyzer = "ik_smart",type = FieldType.String)
    @Field(type = FieldType.keyword,fielddata = true)
    private String tags;  // 标签


    public EsBlog(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public EsBlog(Long blogId, String title, String summary, String content, String username, String avatar,Timestamp createTime,
                  Integer readSize,Integer commentSize, Integer voteSize , String tags) {
        this.blogId = blogId;
        this.title = title;
        this.summary = summary;
        this.content = content;
        this.username = username;
        this.avatar = avatar;
        this.createTime = createTime;
        this.readSize = readSize;
        this.commentSize = commentSize;
        this.voteSize = voteSize;
        this.tags = tags;
    }

    public EsBlog(Blog blog){
        this.blogId = blog.getId();
        this.title = blog.getTitle();
        this.summary = blog.getSummary();
        this.content = blog.getContent();
        this.username = blog.getUser().getUsername();
        this.avatar = blog.getUser().getAvatar();
        this.createTime = blog.getCreateTime();
        this.readSize = blog.getReadSize();
        this.commentSize = blog.getCommentSize();
        this.voteSize = blog.getVoteSize();
        this.tags = blog.getTags();
    }

    public void update(Blog blog){
        this.blogId = blog.getId();
        this.title = blog.getTitle();
        this.summary = blog.getSummary();
        this.content = blog.getContent();
        this.username = blog.getUser().getUsername();
        this.avatar = blog.getUser().getAvatar();
        this.createTime = blog.getCreateTime();
        this.readSize = blog.getReadSize();
        this.commentSize = blog.getCommentSize();
        this.voteSize = blog.getVoteSize();
        this.tags = blog.getTags();
    }

    public String toString() {
        return String.format(
                "User[id=%d, title='%s', content='%s']",
                blogId, title, content);
    }

}
