package ncu.im3069.demo.app;


import org.json.*;

public class Insurance {

    /** 保險編號,資料庫自動產生 */
    private int insurance_id;
    
    /** 保險名稱 */
    private String insurance_name;
    
    /** 保險有效期間 */
    private int duration_period;
    
    /** 保費 */
    private int amount_insured;
    
    /** 保險資訊 */
    private String details;

    /** 刪除記錄欄 
     * 預設為0，若紀錄為1表示該保險已刪除
     * */
	private int delete_key;
	
    /** ih，InsuranceHelper之物件與Insurance相關之資料庫方法（Sigleton） */
    private InsuranceHelper ih =  InsuranceHelper.getHelper();
    
    /**
     * 實例化（Instantiates）一個新的（new）Insurance物件<br>
     * 採用多載（overload）方法進行，此建構子用於建立與更新保險品項
     *
     * @param insurance_name 保險名稱
     * @param duration_period 保險有效期間
     * @param amount_insured 保額
     * @param details 保險資訊
     */
    public Insurance(String insurance_name, int duration_period, int amount_insured, String details) {
        this.insurance_name = insurance_name;
        this.duration_period = duration_period;
        this.amount_insured = amount_insured;
        this.details = details;
        update();
    }
    
    /**
     * 實例化（Instantiates）一個新的（new）Insurance物件<br>
     * 採用多載（overload）方法進行，此建構子用於刪除保險品項
     *
     * @param delete_key 刪除紀錄欄
     */
    public Insurance(int delete_key) {
        this.delete_key = delete_key;
        update();
    }
    
    /**
     * 實例化（Instantiates）一個新的（new）Insurance物件<br>
     * 採用多載（overload）方法進行，此建構子用於查詢保險資料時，將每一筆資料新增為一個保險物件
     *
     * @param insurance_id 保險編號
     * @param insurance_name 保險名稱
     * @param duration_period 保險有效期間
     * @param amount_insured 保額
     * @param details 保險資訊
     * @param delete_key 刪除紀錄欄
     */
    public Insurance(int insurance_id, String insurance_name, int duration_period, int amount_insured, String details, int delete_key) {
        this.insurance_id = insurance_id;
        this.insurance_name = insurance_name;
        this.duration_period = duration_period;
        this.amount_insured = amount_insured;
        this.details = details;
        this.delete_key = delete_key;
    }
    
    /**
     * 取得保險編號
     *
     * @return int 回傳保險編號
     */
	public int getInsuranceID() {
		return this.insurance_id;
	}
	
    /**
     * 取得保險名稱
     *
     * @return String 回傳保險名稱
     */
	public String getInsuranceName() {
		return this.insurance_name;
	}
	
    /**
     * 取得保險有效期間
     *
     * @return int 回傳保險有效期間
     */
	public int getDurationPeriod() {
		return this.duration_period;
	}
	
	/**
     * 取得保額
     *
     * @return int 回傳保額
     */
	public int getAmountInsured() {
		return this.amount_insured;
	}	
	
    /**
     * 取得保險資訊
     *
     * @return int 回傳保險資訊
     */
	public String getDetails() {
		return this.details;
	}
	
	/**
     * 取得刪除紀錄欄
     *
     * @return int 回傳刪除紀錄欄
     */
	public int getDeleteKey() {
		return this.delete_key;
	}

    public JSONObject update() {
        /** 新建一個JSONObject用以儲存更新後之資料 */
        JSONObject data = new JSONObject();
        
        /** 檢查該保險品項是否已經在資料庫 */
        if(this.insurance_id != 0) {
            /** 透過InsuranceHelper物件，更新目前之保險資料至資料庫中 */
            data = ih.update(this);
        }
        
        return data;
    }
    
    /**
     * 取得該保險所有資料
     *
     * @return the data 取得該保險之所有資料並封裝於JSONObject物件內
     */
    public JSONObject getData() {
        /** 透過JSONObject將該保險所需之資料全部進行封裝*/ 
        JSONObject jso = new JSONObject();
        jso.put("insurance_id", getInsuranceID());
        jso.put("insurance_name", getInsuranceName());
        jso.put("duration_period", getDurationPeriod());
        jso.put("amount_insured", getAmountInsured());
        jso.put("details", getDetails());
        jso.put("delete_key", getDeleteKey());

        return jso;
    }

}
