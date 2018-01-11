package foo.dto;


import com.nh.micro.orm.MicroMappingAnno;
import com.nh.micro.orm.MicroTableName;



/**
 * 
 * @author ninghao
 *
 */

@MicroTableName(name="micro_test5")
public class MicroTest5 {
	@MicroMappingAnno(name="id")
	private String id;
	
	@MicroMappingAnno(name="meta_key")
	private String metaKey;
	
	@MicroMappingAnno(name="meta_name")
	private String metaName;
	
	@MicroMappingAnno(name="meta_type")
	private String metaType;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMetaKey() {
		return metaKey;
	}

	public void setMetaKey(String metaKey) {
		this.metaKey = metaKey;
	}

	public String getMetaName() {
		return metaName;
	}

	public void setMetaName(String metaName) {
		this.metaName = metaName;
	}

	public String getMetaType() {
		return metaType;
	}

	public void setMetaType(String metaType) {
		this.metaType = metaType;
	}
}
