package ncu.im3069.demo.app;
import org.json.*;
// TODO: Auto-generated Javadoc
/**
 * The Class Member
 * Member類別（class）具有會員所需要之屬性與方法，並且儲存與會員相關之商業判斷邏輯<br>
 * 
 * @author IPLab
 * @version 1.0.0
 * @since 1.0.0
 */
public class Member {
    
	
	/**
	 * VARIABLES
	 */
    private String 					id;
    private String 					name;
    private String 					email;
    private String 					password;
    private String 					bank_account;
    private String 					birthday;
    private int 					gender;
    private int 					height;
    private int 					weight;
    private int 					disease_id;
    private String 					phone_number;
    private String 					address;
    private int 					manager;
    private int 					delete_key;
    private MemberHelper 			mh = MemberHelper.getHelper();
    
    
    /**
     * FUNCTIONS
     */
    /**
     * 取得會員之身分證字號
     *
     * @return the id 回傳身分證字號
     */
    public String 					getID() {
        return this.id;
    }
    /**
     * 取得會員之姓名
     *
     * @return the name 回傳會員姓名
     */
    public String 					getName() {
        return this.name;
    }
    /**
     * 取得會員之電子郵件信箱
     *
     * @return the email 回傳會員電子郵件信箱
     */
    public String 					getEmail() {
        return this.email;
    }
    /**
     * 取得會員之密碼
     *
     * @return the password 回傳會員密碼
     */
    public String 					getPassword() {
        return this.password;
    }
    /**
     * 取得會員之銀行帳號
     *
     * @return the bank_account 回傳會員銀行帳號
     */
    public String 					getBankAccount() {
        return this.bank_account;
    }
    /**
     * 取得會員之生日
     *
     * @return the birthday 回傳會員生日
     */
    public String 					getBirthday() {
        return this.birthday;
    }
    /**
     * 取得會員之性別
     *
     * @return the gender 回傳會員性別
     */
    public int 						getGender() {
        return this.gender;
    }
    /**
     * 取得會員之身高
     *
     * @return the height 回傳會員身高
     */
    public int 						getHeight() {
        return this.height;
    }
    /**
     * 取得會員之體重
     *
     * @return the weight 回傳會員體重
     */
    public int 						getWeight() {
        return this.weight;
    }
    /**
     * 取得會員之疾病
     *
     * @return the disease_id 回傳會員健康狀況
     */
    public int 						getDiseaseID() {
        return this.disease_id;
    }
    /**
     * 取得會員之電話號碼
     *
     * @return the phone_number 回傳會員電話號碼
     */
    public String 					getPhoneNumber() {
        return this.phone_number;
    }
    /**
     * 取得會員之地址
     *
     * @return the address 回傳會員地址
     */
    public String 					getAddress() {
        return this.address;
    }
    /**
     * 取得會員是否具管理者身分
     *
     * @return the manager 回傳會員是否具管理者身分
     */
    public int 						getManager() {
        return this.manager;
    }
    /**
     * 取得會員資料之會員是否
     *
     * @return the status 回傳會員組別
     */
    public int 						getDeleteKey() {
        return this.delete_key;
    }
    
    
    /*
     * JSONObject
     */
    /**
     * 更新會員資料
     *
     * @return the JSON object 回傳SQL更新之結果與相關封裝之資料
     */
    public JSONObject 				update() {
        /**
         * 新建一個JSONObject用以儲存更新後之資料
         */
        JSONObject 					data 			= new JSONObject();
        /**
         * 檢查該名會員是否已經在資料庫
         *//*
        if(this.id.isEmpty()) {
        	/**
        	 * 透過MemberHelper物件，更新目前之會員資料置資料庫中
        	 *//*
            data = mh.update(this);
            }*/
		return data;
        }
    /**
     * 取得該名會員所有資料
     *
     * @return the data 取得該名會員之所有資料並封裝於JSONObject物件內
     */
    public JSONObject 				getData() {
        /** 透過JSONObject將該名會員所需之資料全部進行封裝*/ 
        JSONObject 					jso 			= new JSONObject();
        jso.put("id", 				getID());
        jso.put("name", 			getName());
        jso.put("email", 			getEmail());
        jso.put("password", 		getPassword());//這個是？
        jso.put("bank_account", 	getBankAccount());
        jso.put("birthday", 		getBirthday());
        jso.put("gender", 			getGender());
        jso.put("height", 			getHeight());
        jso.put("weight", 			getWeight());
        jso.put("disease_id", 		getDiseaseID());
        jso.put("phone_number", 	getPhoneNumber());
        jso.put("address", 			getAddress());
        jso.put("manager", 			getManager());
        jso.put("delete_key", 		getDeleteKey());
        return jso;
    }
    
    
    /**
     * CONSTRUCTORS
     */
    /**
     * 此建構子用於					新增會員資料
     * 時，產生一名新的會員
     *
     * @param id 					會員身分證字號
     * @param name 					會員姓名
     * @param email 				會員電子信箱
     * @param password 				會員密碼
     * @param bank_account 			會員銀行帳戶
     * @param birthday 				會員生日
     * @param gender 				會員性別
     * @param height 				會員身高
     * @param weight 				會員體重
     * @param disease_id 			會員疾病
     * @param phone_number 			會員電話
     * @param address 				會員地址
     */
    public 							Member(
    	String 						id, 
    	String 						name, 
    	String 						email, 
    	String 						password, 
    	String 						bank_account, 
    	String 						birthday, 
    	int 						gender, 
    	int 						height, 
    	int 						weight, 
    	int 						disease_id, 
    	String 						phone_number, 
    	String 						address
    								) {
        this.id 					= id;
        this.name 					= name;
    	this.email 					= email;
        this.password 				= password;
        this.bank_account 			= bank_account;
        this.birthday 				= birthday;
        this.gender 				= gender;
        this.height 				= height;
        this.weight 				= weight;
        this.disease_id 			= disease_id;
        this.phone_number 			= phone_number;
        this.address 				= address;
        update();
    }
    /**
     * 此建構子用於					更新會員資料
     * 時，產生一名會員
     * 
     * @param id 					會員身分證字號
     * @param name 					會員姓名
     * @param email 				會員電子信箱
     * @param password 				會員密碼
     * @param bank_account 			會員銀行帳戶
     * @param birthday 				會員生日
     * @param gender 				會員性別
     * @param height 				會員身高
     * @param weight 				會員體重
     * @param disease_id 			會員疾病
     * @param phone_number 			會員電話
     * @param address 				會員地址
     * 
     * 
     */
    public 							Member(
    	String 						name, 
    	String 						email, 
    	String 						password, 
    	String 						bank_account, 
    	String 						birthday, 
    	int 						gender, 
    	int 						height, 
    	int 						weight, 
    	int 						disease_id, 
    	String 						phone_number, 
    	String 						address, 
    	int 						manager, 
    	int 						delete_key
    								) {
        this.name 					= name;
    	this.email 					= email;
        this.password 				= password;
        this.bank_account 			= bank_account;
        this.birthday 				= birthday;
        this.gender 				= gender;
        this.height 				= height;
        this.weight 				= weight;
        this.disease_id 			= disease_id;
        this.phone_number 			= phone_number;
        this.address 				= address;
        this.manager				= manager;
        this.delete_key				= delete_key;
        update();
    }
    /**
     * 此建構子用於					取得會員資料
     * 時，將每一筆資料新增為一個會員物件
     *
     * @param id 					會員編號
     * @param email 				會員電子信箱
     * @param password 				會員密碼
     * @param name 					會員姓名
     * @param bank_account 			會員銀行帳戶
     * @param birthday 				會員生日
     * @param gender 				會員性別
     * @param height 				會員身高
     * @param weight 				會員體重
     * @param disease_id 			會員疾病
     * @param phone_number 			會員電話
     * @param address 				會員地址
     * @param manager 				會員是否具管理者身分
     * @param delete_key 			會員是否要被刪除
     */
    public 							Member(
    	String 						id, 
    	String 						name, 
    	String 						email, 
    	String 						password, 
    	String 						bank_account, 
    	String 						birthday, 
    	int 						gender, 
    	int 						height, 
    	int 						weight, 
    	int 						disease_id, 
    	String 						phone_number, 
    	String 						address, 
    	int 						manager, 
    	int 						delete_key
    								) {
        this.id 					= id;
        this.name 					= name;
        this.email 					= email;
        //this.password 				= password;
        this.bank_account 			= bank_account;
        this.birthday 				= birthday;
        this.gender 				= gender;
        this.height 				= height;
        this.weight 				= weight;
        this.disease_id 			= disease_id;
        this.phone_number 			= phone_number;
        this.address 				= address;
        this.manager 				= manager;
        this.delete_key 			= delete_key;
    }
   


}