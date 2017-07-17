package groovy.product.algo;

public interface ProductAlgoConst {

	
	/**
	 * productId 产品id
	 */
	public static final String productId="productId";
	
	/**
	 * contractAmt 合同金额\合同额
	 */
	public static final String contractAmt="contractAmt";
	
	/**
	 * payAmt 放款金额
	 */
	public static final String payAmt="payAmt";	
	
	/**
	 * singleGeneralRate 一次性综合费率\首期收费利率
	 */
	public static final String singleGeneralRate="singleGeneralRate";		
	
	/**
	 * monthRate 月利率
	 */
	public static final String monthRate="monthRate";

	/**
	 * monthlyRepaymentRate 每月还款利率
	 */
	public static final String monthlyRepaymentRate="monthlyRepaymentRate";	
	
	/**
	 * feeRate 手续费费率（日）
	 */
	public static final String feeRate="feeRate";

	/**
	 * contractPeriods 分期数\合同期数
	 */
	public static final String contractPeriods="contractPeriods";
	
	/**
	 * monthAverageRate 综合费率（月）\月综合费率
	 */
	public static final String monthAverageRate="monthAverageRate";	
	
	/**
	 * payActDate 实际放款日期
	 */
	public static final String payActDate="payActDate";		

	/**
	 * payDate 放款日期
	 */
	public static final String payDate="payDate";		
	
	/**
	 * repayDay 还款日\账单日
	 */
	public static final String repayDay="repayDay";	
	
	/**
	 * dueDate 到期日
	 */
	public static final String dueDate="dueDate";		
	
	/**
	 * firstInterestPayDate 首期利息支付时间
	 */
	public static final String firstInterestPayDate="firstInterestPayDate";	

	/**
	 * startRepayTime 开始还款时间(首次收取服务费时间)
	 */
	public static final String startRepayTime="startRepayTime";		
	
	/**
	 * contractId 合同id
	 */
	public static final String contractId="contractId";		
	
	/**
	 * serviceCharge 总服务费
	 */
	public static final String serviceCharge="serviceCharge";		
	
	/**
	 * firstServiceFee 前期服务费\首期服务费
	 */
	public static final String firstServiceFee="firstServiceFee";		
	
	/**
	 * monthServiceAmount 期服务费
	 */
	public static final String monthServiceAmount="monthServiceAmount";	
	
	/**
	 * currentShouldAmount 每期本金\当月应收本金
	 */
	public static final String currentShouldAmount="currentShouldAmount";		
	
	/**
	 * currentShouldInterest 期利息\当月应收利息\月利息
	 */
	public static final String currentShouldInterest="currentShouldInterest";			
	
	/**
	 * firstInterest 首期利息
	 */
	public static final String firstInterest="firstInterest";	

	/**
	 * grossInterest 总利息
	 */
	public static final String grossInterest="grossInterest";		

	/**
	 * repayDateList 还款日
	 */
	public static final String repayDateList="repayDateList";	

	/**
	 * period 期数
	 */
	public static final String period="period";
	
	/**
	 * capital 本金
	 */
	public static final String capital="capital";	
	
	/**
	 * interest 利息
	 */
	public static final String interest="interest";	

	/**
	 * serviceFee 服务费
	 */
	public static final String serviceFee="serviceFee";		

	/**
	 * repayDate 还款日
	 */
	public static final String repayDate="repayDate";	
	
	
	
	/**
	 * isNoMonth 是否足月
	 */
	public static final String isNoMonth="isNoMonth";	
	
	
	/**
	 * periodType 期类别(第0期:zero 首期:first 中间期:mid 尾期:last)
	 */
	public static final String periodType="periodType";	
	
	/**
	 * periodType 期类别 第0期:zero 
	 */
	public static final String periodType_zero="zero";
	
	/**
	 * periodType 期类别 首期:first
	 */
	public static final String periodType_first="first";
	
	/**
	 * periodType 期类别    中间期:mid
	 */
	public static final String periodType_mid="mid";
	
	/**
	 * periodType 期类别 尾期:last 
	 */
	public static final String periodType_last="last";	
	
	/**
	 * firstPeriodDays 首期实际天数 
	 */	
	public static final String firstPeriodDays="firstPeriodDays";

	/**
	 * productLixiAlgId 利息算法Id 
	 */	
	public static final String productLixiAlgId="productLixiAlgId";
	
	/**
	 * productBenjinAlgId 本金算法Id 
	 */	
	public static final String productBenjinAlgId="productBenjinAlgId";	

	/**
	 * productFuwufeiAlgId 服务费算法Id 
	 */	
	public static final String productFuwufeiAlgId="productFuwufeiAlgId";	
	
	/**
	 * isLixiUpFlag 利息砍头期flag 
	 */	
	public static final String isLixiUpFlag="isLixiUpFlag";	
	
	/**
	 * isBenjinUpFlag 本金砍头期flag 
	 */	
	public static final String isBenjinUpFlag="isBenjinUpFlag";	
	
	/**
	 * isFuwufeiUpFlag 服务费砍头期flag 
	 */	
	public static final String isFuwufeiUpFlag="isFuwufeiUpFlag";	
	
	/**
	 * productPhaseList 产品阶段list 
	 */	
	public static final String productPhaseList="productPhaseList";		
	
}
