package groovy.model; 

import com.nh.micro.db.IMicroDbModel;
import com.nh.micro.db.MicroDbModelEntry;


public class MicroBaseModel extends IMicroDbModel {
	/**
	 * 通用表字段
	 */
	public static final MicroDbModelEntry id=new MicroDbModelEntry("id","主键","meta_content","默认主键",String.class,false);
	public static final MicroDbModelEntry meta_key=new MicroDbModelEntry("meta_key","编号","meta_content","元编号",String.class,false);
	public static final MicroDbModelEntry meta_name=new MicroDbModelEntry("meta_name","名称","meta_content","元名称",String.class,false);
	public static final MicroDbModelEntry meta_type=new MicroDbModelEntry("meta_type","父编号","meta_content","父编号",String.class,false);
	public static final MicroDbModelEntry meta_content=new MicroDbModelEntry("meta_content","内容","meta_content","内容",String.class,false);
	public static final MicroDbModelEntry remark=new MicroDbModelEntry("remark","remark","meta_content","备注",String.class,false);
	public static final MicroDbModelEntry update_time=new MicroDbModelEntry("update_time","更新时间","meta_content","更新时间",String.class,false);
	public static final MicroDbModelEntry create_time=new MicroDbModelEntry("create_time","创建时间","meta_content","创建时间",String.class,false);

}
