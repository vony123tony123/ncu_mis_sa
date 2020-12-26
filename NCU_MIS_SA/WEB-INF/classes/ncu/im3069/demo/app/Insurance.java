package ncu.im3069.demo.app;

import org.json.*;

public class Insurance {

    /** id，保險編號 */
    private int insurance_id;

    /** name，保險名稱 */
    private String insurance_name;

    /** period，要保日期 */
    private String duration_period;
    
    /** amount，保額 */
    private int amount_insured;

    /** details，詳細資訊 */
    private String details;
	

    /**
     * 實例化（Instantiates）一個新的（new）Insurance 物件<br>
     * 採用多載（overload）方法進行，此建構子用於新增保險時
     *
     * @param id 保險編號
     */
	public Insurance(int insurance_id) {
		this.insurance_id = insurance_id;
	}

    /**
     * 實例化（Instantiates）一個新的（new）Insurance 物件<br>
     * 採用多載（overload）方法進行，此建構子用於新增保險時
     *
     * @param name 保險名稱
     * @param period 年限
     * @param amount 保額
     * @param details 資訊細節
     */
	public Insurance(String name, String duration_period, int amount, String details) {
		this.insurance_name = insurance_name;
		this.duration_period = duration_period;
		this.amount_insured = amount_insured;
		this.details = details;
	}

    /**
     * 實例化（Instantiates）一個新的（new）Insurance 物件<br>
     * 採用多載（overload）方法進行，此建構子用於修改保險時
     *
     * @param id 保險編號
     * @param name 保險名稱
     * @param period 保險年限
     * @param amount 保額
     * @param details 資訊細節
     */
	public Insurance(int id, String name, String period, int amount, String details) {
		this.insurance_id = insurance_id;
		this.insurance_name = insurance_name;
		this.duration_period = period;
		this.amount_insured = amount_insured;
		this.details = details;
	}

    /**
     * 取得保險編號
     *
     * @return int 回傳保險編號
     */
	public int getID() {
		return this.insurance_id;
	}

    /**
     * 取得保險名稱
     *
     * @return String 回傳保險名稱
     */
	public String getName() {
		return this.insurance_name;
	}

    /**
     * 取得保險年限
     *
     * @return double 回傳保險年限
     */
	public String getPeriod() {
		return this.duration_period;
	}

    /**
     * 取得保額
     *
     * @return String 回傳保額
     */
	public int getAmount() {
		return this.amount_insured;
	}

    /**
     * 取得保險資訊細節
     *
     * @return String 回傳保險資訊細節
     */
	public String getDetails() {
		return this.details;
	}

    /**
     * 取得保險資訊
     *
     * @return JSONObject 回傳保險資訊
     */
	public JSONObject getData() {
        /** 透過JSONObject將該項保險所需之資料全部進行封裝*/
        JSONObject jso = new JSONObject();
        jso.put("id", getID());
        jso.put("name", getName());
        jso.put("period", getPeriod());
        jso.put("amount", getAmount());
        jso.put("details", getDetails());

        return jso;
    }
}
