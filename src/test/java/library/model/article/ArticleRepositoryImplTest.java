package library.model.article;

import org.example.exception.ItemNotFoundException;
import org.example.library.model.library.article.Article;
import org.example.library.model.library.article.ArticleRepositoryImpl;
import org.junit.Before;
import org.junit.Test;
import utils.TestUtils;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class ArticleRepositoryImplTest {

    private final ArticleRepositoryImpl articleRepository = ArticleRepositoryImpl.getInstance();

    @Before
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

        articleRepository.saveAll(new Article[]{article1, article2}, Article.class);

        var result = articleRepository.getAll();
        assertThat(result).hasSize(2);
        assertThat(result[0].getTitle()).isEqualTo(article1.getTitle());
        assertThat(result[1].getTitle()).isEqualTo(article2.getTitle());
    }

    @Test
    public void saveAll_updates() throws SQLException {
        ArticleRepositoryImpl articleRepo = ArticleRepositoryImpl.getInstance();
        var article = TestUtils.createArticle("title1");
        var article2 = TestUtils.createArticle("title2");
        articleRepo.saveAll(new Article[]{article, article2}, Article.class);
        article.setTitle("updated title 1");
        article2.setTitle("updated title 2");

        articleRepo.saveAll(new Article[]{article, article2}, Article.class);

        var result = articleRepo.getAll();
        assertThat(result).hasSize(2);
        assertThat(result[0].getTitle()).isEqualTo(article.getTitle());
        assertThat(result[1].getTitle()).isEqualTo(article2.getTitle());
    }

    @Test
    public void saveAll_saves_new_and_updates_existing() throws SQLException {
        ArticleRepositoryImpl articleRepo = ArticleRepositoryImpl.getInstance();
        var article = TestUtils.createArticle("title1");
        var article2 = TestUtils.createArticle("title2");
        articleRepo.save(article2);
        article2.setTitle("updated title 2");

        articleRepo.saveAll(new Article[]{article, article2}, Article.class);

        var result = articleRepo.getAll();
        assertThat(result).hasSize(2);
        assertThat(result).anySatisfy(item -> {
            assertThat(item.getTitle()).isEqualTo(article2.getTitle());
            assertThat(item.getId()).isEqualTo(article2.getId());
        });
        assertThat(result).anySatisfy(item -> assertThat(item.getTitle()).isEqualTo(article.getTitle()));
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
