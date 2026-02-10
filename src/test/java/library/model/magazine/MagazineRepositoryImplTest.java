package library.model.magazine;

import org.example.exception.ItemNotFoundException;
import org.example.library.model.article.Article;
import org.example.library.model.magazine.Magazine;
import org.example.library.model.magazine.MagazineRepositoryImpl;
import org.junit.After;
import org.junit.Test;
import utils.TestUtils;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class MagazineRepositoryImplTest {
    private final MagazineRepositoryImpl magazineRepository = MagazineRepositoryImpl.getInstance();

    @After
    public void cleanup() throws SQLException {
        magazineRepository.removeAll();
    }

    @Test
    public void create_magazine_works_correctly() throws SQLException, ItemNotFoundException {
        var magazine = TestUtils.createMagazine();

        magazineRepository.save(magazine);

        var found = magazineRepository.getOne(magazine.getId());

        assertThat(magazine.getTitle()).isEqualTo(found.getTitle());
        assertThat(magazine.getAuthor()).isEqualTo(found.getAuthor());
        assertThat(magazine.getContent()).isEqualTo(found.getContent());

    }

    @Test
    public void saveAll_works_correctly() throws SQLException {
        var magazine1 = TestUtils.createMagazine("title1");
        var magazine2 = TestUtils.createMagazine("title2");

        var result = magazineRepository.saveAll(new Magazine[]{magazine1, magazine2}, Magazine.class);

        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(magazine1, magazine2);
        assertThat(result[0].getTitle()).isEqualTo(magazine1.getTitle());
        assertThat(result[1].getTitle()).isEqualTo(magazine2.getTitle());
    }

    @Test
    public void get_one_works_correctly() throws SQLException, ItemNotFoundException {

        var magazine = TestUtils.createMagazine();
        magazineRepository.save(magazine);

        var found = magazineRepository.getOne(magazine.getId());

        assertThat(found.getTitle()).isEqualTo(magazine.getTitle());
        assertThat(found.getAuthor()).isEqualTo(magazine.getAuthor());
        assertThat(found.getContent()).isEqualTo(magazine.getContent());
    }

    @Test
    public void get_all_works_correctly() throws SQLException, ItemNotFoundException {

        var magazine = TestUtils.createMagazine();
        var magazine2 = TestUtils.createMagazine();
        magazineRepository.save(magazine);
        magazineRepository.save(magazine2);

        var found = magazineRepository.getAll();

        assertThat(found.length).isEqualTo(2);

    }

    @Test
    public void remove_one_works_correctly() throws SQLException, ItemNotFoundException {

        var magazine = TestUtils.createMagazine();
        var magazine2 = TestUtils.createMagazine();
        magazineRepository.save(magazine);
        magazineRepository.save(magazine2);

        var result = magazineRepository.removeOne(magazine);

        assertThat(result).isTrue();
        var found = magazineRepository.getAll();
        assertThat(found.length).isEqualTo(1);
        assertThat(found[0].getTitle()).isEqualTo(magazine.getTitle());
        assertThat(found[0].getAuthor()).isEqualTo(magazine.getAuthor());
        assertThat(found[0].getContent()).isEqualTo(magazine.getContent());
    }

    @Test
    public void remove_one_by_id_works_correctly() throws SQLException, ItemNotFoundException {

        var magazine = TestUtils.createMagazine();
        var magazine2 = TestUtils.createMagazine();
        magazineRepository.save(magazine);
        magazineRepository.save(magazine2);

        var result = magazineRepository.removeOne(magazine.getId());

        assertThat(result).isTrue();
        var found = magazineRepository.getAll();
        assertThat(found.length).isEqualTo(1);
        assertThat(found[0].getTitle()).isEqualTo(magazine.getTitle());
        assertThat(found[0].getAuthor()).isEqualTo(magazine.getAuthor());
        assertThat(found[0].getContent()).isEqualTo(magazine.getContent());
    }

}
