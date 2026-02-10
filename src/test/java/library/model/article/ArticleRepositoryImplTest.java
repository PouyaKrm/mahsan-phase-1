package library.model.article;

import org.example.exception.ItemNotFoundException;
import org.example.library.model.article.Article;
import org.example.library.model.article.ArticleRepositoryImpl;
import org.example.library.model.book.Book;
import org.example.library.model.book.BookRepositoryImpl;
import org.junit.After;
import org.junit.Test;
import utils.TestUtils;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class ArticleRepositoryImplTest {
    private final ArticleRepositoryImpl articleRepository = ArticleRepositoryImpl.getInstance();

    @After
    public void cleanup() throws SQLException {
        articleRepository.removeAll();
    }

    @Test
    public void create_article_works_correctly() throws SQLException, ItemNotFoundException {
        var article = TestUtils.createArticle();

        articleRepository.save(article);

        var found = articleRepository.getOne(article.getId());

        assertThat(article.getTitle()).isEqualTo(found.getTitle());
        assertThat(article.getAuthor()).isEqualTo(found.getAuthor());
        assertThat(article.getContent()).isEqualTo(found.getContent());

    }

    @Test
    public void saveAll_works_correctly() throws SQLException {
        var article1 = TestUtils.createArticle("title1");
        var article2 = TestUtils.createArticle("title2");

        var result = articleRepository.saveAll(new Article[]{article1, article2}, Article.class);

        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(article1, article2);
        assertThat(result[0].getTitle()).isEqualTo(article1.getTitle());
        assertThat(result[1].getTitle()).isEqualTo(article2.getTitle());
    }

    @Test
    public void get_one_works_correctly() throws SQLException, ItemNotFoundException {

        var article = TestUtils.createArticle();
        articleRepository.save(article);

        var found = articleRepository.getOne(article.getId());

        assertThat(found.getTitle()).isEqualTo(article.getTitle());
        assertThat(found.getAuthor()).isEqualTo(article.getAuthor());
        assertThat(found.getContent()).isEqualTo(article.getContent());
    }

    @Test
    public void get_all_works_correctly() throws SQLException, ItemNotFoundException {

        var article = TestUtils.createArticle();
        var article2 = TestUtils.createArticle();
        articleRepository.save(article);
        articleRepository.save(article2);

        var found = articleRepository.getAll();

        assertThat(found.length).isEqualTo(2);

    }

    @Test
    public void remove_one_works_correctly() throws SQLException, ItemNotFoundException {

        var article = TestUtils.createArticle();
        var article2 = TestUtils.createArticle();
        articleRepository.save(article);
        articleRepository.save(article2);

        var result = articleRepository.removeOne(article);

        assertThat(result).isTrue();
        var found = articleRepository.getAll();
        assertThat(found.length).isEqualTo(1);
        assertThat(found[0].getTitle()).isEqualTo(article.getTitle());
        assertThat(found[0].getAuthor()).isEqualTo(article.getAuthor());
        assertThat(found[0].getContent()).isEqualTo(article.getContent());
    }

    @Test
    public void remove_one_by_id_works_correctly() throws SQLException, ItemNotFoundException {

        var article = TestUtils.createArticle();
        var article2 = TestUtils.createArticle();
        articleRepository.save(article);
        articleRepository.save(article2);

        var result = articleRepository.removeOne(article.getId());

        assertThat(result).isTrue();
        var found = articleRepository.getAll();
        assertThat(found.length).isEqualTo(1);
        assertThat(found[0].getTitle()).isEqualTo(article.getTitle());
        assertThat(found[0].getAuthor()).isEqualTo(article.getAuthor());
        assertThat(found[0].getContent()).isEqualTo(article.getContent());
    }
}
