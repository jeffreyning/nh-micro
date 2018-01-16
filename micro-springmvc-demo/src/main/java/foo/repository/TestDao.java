package foo.repository;

import java.util.List;
import java.util.Map;

import com.nh.micro.dao.mapper.ListInnerClass;
import com.nh.micro.dao.mapper.MicroCommonMapper;
import com.nh.micro.dao.mapper.MicroPageInfo;
import com.nh.micro.orm.MicroDbName;

import foo.dto.MicroTest5;


/**
 * 
 * @author ninghao
 *
 */
@MicroDbName
public interface TestDao extends MicroCommonMapper<MicroTest5> {
	
	public int updateInfo(Map paramMap);
	
	public int insertInfo(Map paramMap);
	
	@ListInnerClass(name=MicroTest5.class)
	public List<MicroTest5> queryInfosByPage(Map paramMap,MicroPageInfo pageInfo);	

	public MicroTest5 queryInfoById(Map paramMap);	


}
