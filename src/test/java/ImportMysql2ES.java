import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zhangyuhao.dao.ArticleMapper;
import com.zhangyuhao.dao.ArticleRep;
import com.zhangyuhao.entity.Article;
import com.zhangyuhao.service.ArticleService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-beans.xml")
public class ImportMysql2ES {

	@Autowired
	ArticleMapper mapper;
	
	@Autowired
	ArticleRep rep;
	
	@Test
	public void importMysql2Es(){
		//1.从mysql中查询已经审核通过的所有 文章
		List<Article> findAllArticles = mapper.findAllArticles(1);
		//2.查询出来的文章保存es索引
		rep.saveAll(findAllArticles);
	}
}
