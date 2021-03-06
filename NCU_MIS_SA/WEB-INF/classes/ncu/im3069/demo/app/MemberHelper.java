package ncu.im3069.demo.app;

import java.sql.*;
import java.time.LocalDateTime;
import org.json.*;
import ncu.im3069.demo.util.DBMgr;

// TODO: Auto-generated Javadoc
/**
 * The Class MemberHelper MemberHelper類別（class）主要管理所有與Member相關與資料庫之方法（method）
 * 
 * @author IPLab
 * @version 1.0.0
 * @since 1.0.0
 */
public class MemberHelper {

	/**
	 * VARIABLES
	 */
	/**
	 * 實例化（Instantiates）一個新的（new）MemberHelper物件<br>
	 * 採用Singleton不需要透過new
	 */
	private MemberHelper() {
	}

	/**
	 * 靜態變數，儲存MemberHelper物件
	 */
	private static MemberHelper mh;
	/**
	 * 儲存JDBC資料庫連線
	 */
	private Connection conn = null;
	/**
	 * 儲存JDBC預準備之SQL指令
	 */
	private PreparedStatement pres = null;

	/**
	 * FUNCTIONS
	 */
	/**
	 * 實作Singleton（單例模式）， 僅允許建立一個MemberHelper物件
	 *
	 * @return the helper 回傳MemberHelper物件
	 */
	public static MemberHelper getHelper() {
		/**
		 * Singleton檢查是否已經有MemberHelper物件， 若無則new一個，若有則直接回傳
		 */
		if (mh == null) {
			mh = new MemberHelper();
		}
		return mh;
	}

	/**
	 * 透過會員身分證字號（index_id） 刪除會員
	 *
	 * @param id 會員編號
	 * @return the JSONObject 回傳SQL執行結果
	 */
	public JSONObject deleteByID(String ID_number) {
		/**
		 * 記錄實際執行之SQL指令
		 */
		String exexcute_sql = "";
		/**
		 * 紀錄程式開始執行時間
		 */
		long start_time = System.nanoTime();
		/**
		 * 紀錄SQL總行數
		 */
		int row = 0;
		/**
		 * 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料
		 */
		ResultSet rs = null;
		try {
			/**
			 * 取得資料庫之連線
			 */
			conn = DBMgr.getConnection();
			/**
			 * SQL指令
			 */
			String sql = " Update `missa`.`member` SET `delete_key` = 1 WHERE `ID_number` = ? AND `delete_key` = 0 LIMIT 1";
			/**
			 * 將參數回填至SQL指令當中
			 */
			pres = conn.prepareStatement(sql);
			pres.setString(1, ID_number);
			/**
			 * 執行刪除之SQL指令並記錄影響之行數
			 */
			row = pres.executeUpdate();
			/**
			 * 紀錄真實執行的SQL指令，並印出
			 */
			exexcute_sql = pres.toString();
			System.out.println(exexcute_sql);
		} catch (SQLException e) {
			/**
			 * 印出JDBC SQL指令錯誤
			 */
			System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
		} catch (Exception e) {
			/**
			 * 若錯誤則印出錯誤訊息
			 */
			e.printStackTrace();
		} finally {
			/**
			 * 關閉連線並釋放所有資料庫相關之資源
			 */
			DBMgr.close(rs, pres, conn);
		}
		/**
		 * 紀錄程式結束執行時間
		 */
		long end_time = System.nanoTime();
		/**
		 * 紀錄程式執行時間
		 */
		long duration = (end_time - start_time);
		/**
		 * 將SQL指令、花費時間與影響行數，封裝成JSONObject回傳
		 */
		JSONObject response = new JSONObject();
		response.put("sql", exexcute_sql);
		response.put("row", row);
		response.put("time", duration);
		return response;
	}

	/**
	 * （所有） 取得會員資料
	 *
	 * @return the JSONObject 回傳SQL執行結果與自資料庫取回之所有資料
	 */
	public JSONObject getAll() {
		/**
		 * 新建一個 Member 物件之 m 變數， 用於紀錄每一位查詢回之會員資料
		 */
		Member m = null;
		/**
		 * 用於儲存所有檢索回之會員， 以JSONArray方式儲存
		 */
		JSONArray jsa = new JSONArray();
		/**
		 * 記錄實際執行之SQL指令
		 */
		String exexcute_sql = "";
		/**
		 * 紀錄程式開始執行時間
		 */
		long start_time = System.nanoTime();
		/**
		 * 紀錄SQL總行數
		 */
		int row = 0;
		/**
		 * 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料
		 */
		ResultSet rs = null;
		try {
			/**
			 * 取得資料庫之連線
			 */
			conn = DBMgr.getConnection();
			/**
			 * SQL指令
			 */
			String sql = "SELECT * FROM `missa`.`member` WHERE `delete_key` = 0 ";
			/**
			 * 將參數回填至SQL指令當中，若無則不用只需要執行 prepareStatement
			 */
			pres = conn.prepareStatement(sql);
			/**
			 * 執行查詢之SQL指令並記錄其回傳之資料
			 */
			rs = pres.executeQuery();
			/**
			 * 紀錄真實執行的SQL指令，並印出
			 */
			exexcute_sql = pres.toString();
			System.out.println(exexcute_sql);
			/**
			 * 透過 while 迴圈移動pointer，取得每一筆回傳資料
			 */
			while (rs.next()) {
				/**
				 * 每執行一次迴圈表示有一筆資料
				 */
				row += 1;
				/**
				 * 將 ResultSet 之資料取出
				 */
				String id = rs.getString("ID_number");
				String name = rs.getString("name");
				String email = rs.getString("email");
				String password = rs.getString("password");
				String bank_account = rs.getString("bank_account");
				String birthday = rs.getString("birthday");
				int gender = rs.getInt("gender");
				int height = rs.getInt("height");
				int weight = rs.getInt("weight");
				int disease_id = rs.getInt("disease_id");
				String phone_number = rs.getString("phone_number");
				String address = rs.getString("address");
				int manager = rs.getInt("manager");
				int delete_key = rs.getInt("delete_key");
				
				System.out.println(password);
				
				/**
				 * 將每一筆會員資料產生一名新Member物件
				 */
				
				m = new Member(id, name, email, password, bank_account, birthday, gender, height, weight, disease_id,
						phone_number, address, manager, delete_key);
				/**
				 * 取出該名會員之資料並封裝至 JSONArray 內
				 */
				jsa.put(m.getData());
			}
		} catch (SQLException e) {
			/**
			 * 印出JDBC SQL指令錯誤
			 */
			System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
		} catch (Exception e) {
			/**
			 * 若錯誤則印出錯誤訊息
			 */
			e.printStackTrace();
		} finally {
			/**
			 * 關閉連線並釋放所有資料庫相關之資源
			 */
			DBMgr.close(rs, pres, conn);
		}
		/**
		 * 紀錄程式結束執行時間
		 */
		long end_time = System.nanoTime();
		/**
		 * 紀錄程式執行時間
		 */
		long duration = (end_time - start_time);
		/**
		 * 將SQL指令、花費時間、影響行數與所有會員資料之JSONArray，封裝成JSONObject回傳
		 */
		JSONObject response = new JSONObject();
		response.put("sql", exexcute_sql);
		response.put("row", row);
		response.put("time", duration);
		response.put("data", jsa);
		return response;
	}

	/**
	 * 透過會員身分證字號（index_id） 取得會員資料
	 *
	 * @param id 會員編號
	 * @return the JSON object 回傳SQL執行結果與該會員編號之會員資料
	 */
	public JSONObject getByID(String id) {
		/**
		 * 新建一個 Member 物件之 m 變數，用於紀錄每一位查詢回之會員資料
		 */
		Member m = null;
		/**
		 * 用於儲存所有檢索回之會員，以JSONArray方式儲存
		 */
		JSONArray jsa = new JSONArray();
		/**
		 * 記錄實際執行之SQL指令
		 */
		String exexcute_sql = "";
		/**
		 * 紀錄程式開始執行時間
		 */
		long start_time = System.nanoTime();
		/**
		 * 紀錄SQL總行數
		 */
		int row = 0;
		/**
		 * 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料
		 */
		ResultSet rs = null;
		try {
			/**
			 * 取得資料庫之連線
			 */
			conn = DBMgr.getConnection();
			/**
			 * SQL指令
			 */
			String sql = "SELECT * FROM `missa`.`member` WHERE `ID_number` = ? AND `delete_key` = 0  LIMIT 1";
			/**
			 * 將參數回填至SQL指令當中
			 */
			pres = conn.prepareStatement(sql);
			pres.setString(1, id);
			/**
			 * 執行查詢之SQL指令並記錄其回傳之資料
			 */
			rs = pres.executeQuery();
			/**
			 * 紀錄真實執行的SQL指令，並印出
			 */
			exexcute_sql = pres.toString();
			System.out.println(exexcute_sql);
			/**
			 * 透過 while 迴圈移動pointer，取得每一筆回傳資料
			 */
			/**
			 * 正確來說資料庫只會有一筆該會員編號之資料，因此其實可以不用使用 while 迴圈
			 */
			while (rs.next()) {
				/**
				 * 每執行一次迴圈表示有一筆資料
				 */
				row += 1;
				/**
				 * 將 ResultSet 之資料取出
				 */
				String ID_number = rs.getString("ID_number");
				String name = rs.getString("name");
				String email = rs.getString("email");
				String password = rs.getString("password");
				String bank_account = rs.getString("bank_account");
				String birthday = rs.getString("birthday");
				int gender = rs.getInt("gender");
				int height = rs.getInt("height");
				int weight = rs.getInt("weight");
				int disease_id = rs.getInt("disease_id");
				String phone_number = rs.getString("phone_number");
				String address = rs.getString("address");
				int manager = rs.getInt("manager");
				int delete_key = rs.getInt("delete_key");
				/**
				 * 將每一筆會員資料產生一名新Member物件
				 */
				m = new Member(ID_number, name, email, password, bank_account, birthday, gender, height, weight, disease_id,
						phone_number, address, manager, delete_key);
				/**
				 * 取出該名會員之資料並封裝至 JSONArray 內
				 */
				jsa.put(m.getData());
			}
		} catch (SQLException e) {
			/**
			 * 印出JDBC SQL指令錯誤
			 */
			System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
		} catch (Exception e) {
			/**
			 * 若錯誤則印出錯誤訊息
			 */
			e.printStackTrace();
		} finally {
			/**
			 * 關閉連線並釋放所有資料庫相關之資源
			 */
			DBMgr.close(rs, pres, conn);
		}
		/**
		 * 紀錄程式結束執行時間
		 */
		long end_time = System.nanoTime();
		/**
		 * 紀錄程式執行時間
		 */
		long duration = (end_time - start_time);
		/**
		 * 將SQL指令、花費時間、影響行數與所有會員資料之JSONArray，封裝成JSONObject回傳
		 */
		JSONObject response = new JSONObject();
		response.put("sql", exexcute_sql);
		response.put("row", row);
		response.put("time", duration);
		response.put("data", jsa);
		return response;
	}

	/**
	 * 檢查該名會員之電子郵件信箱是否重複註冊
	 *
	 * @param m 一名會員之Member物件
	 * @return boolean 若重複註冊回傳False，若該信箱不存在則回傳True
	 */
	public boolean checkDuplicate(Member m) {
		/**
		 * 紀錄SQL總行數，若為「-1」代表資料庫檢索尚未完成
		 */
		int row = -1;
		/**
		 * 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料
		 */
		ResultSet rs = null;
		try {
			/**
			 * 取得資料庫之連線
			 */
			conn = DBMgr.getConnection();
			/**
			 * SQL指令
			 */
			String sql = "SELECT count(*) FROM " + "`missa`.`member` " + "WHERE `email` = ? AND `delete_key` = 0 ";
			/**
			 * 取得所需之參數
			 */
			String email = m.getEmail();
			/**
			 * 將參數回填至SQL指令當中
			 */
			pres = conn.prepareStatement(sql);
			pres.setString(1, email);
			/**
			 * 執行查詢之SQL指令並記錄其回傳之資料
			 */
			rs = pres.executeQuery();
			/**
			 * 讓指標移往最後一列，取得目前有幾行在資料庫內
			 */
			rs.next();
			row = rs.getInt("count(*)");
			System.out.print(row);
		} catch (SQLException e) {
			/**
			 * 印出JDBC SQL指令錯誤
			 */
			System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
		} catch (Exception e) {
			/**
			 * 若錯誤則印出錯誤訊息
			 */
			e.printStackTrace();
		} finally {
			/**
			 * 關閉連線並釋放所有資料庫相關之資源
			 */
			DBMgr.close(rs, pres, conn);
		}
		/**
		 * 判斷是否已經有一筆該電子郵件信箱之資料 若無一筆則回傳False，否則回傳True
		 */
		return (row == 0) ? false : true;
	}

	/**
	 * 建立該名會員至資料庫
	 *
	 * @param m 一名會員之Member物件
	 * @return the JSON object 回傳SQL指令執行之結果
	 */
	public JSONObject create(Member m) {
		/**
		 * 記錄實際執行之SQL指令
		 */
		String exexcute_sql = "";
		/**
		 * 紀錄程式開始執行時間
		 */
		long start_time = System.nanoTime();
		/**
		 * 紀錄SQL總行數
		 */
		int row = 0;
		try {
			/**
			 * 取得資料庫之連線
			 */
			conn = DBMgr.getConnection();
			/**
			 * QL指令
			 */
			String sql = "INSERT INTO `missa`.`member`" + "(" + "`ID_number`, " + "`name`, " + "`email`, "
					+ "`password`, " + "`bank_account`, " + "`birthday`, " + "`gender`, " + "`height`, " + "`weight`, "
					+ "`disease_id`, " + "`phone_number`, " + "`address`, " + "`manager`, " 
					+ "`delete_key`" + ")" + " VALUES(?, ?, ?, ?, ?, ?, " + "?, ?, ?, ?, ?, ?, ?, ?)";
			/**
			 * 取得所需之參數
			 */
			String id = m.getID();
			String name = m.getName();
			String email = m.getEmail();
			String password = m.getPassword();
			String bank_account = m.getBankAccount();
			String birthday = m.getBirthday();
			int gender = m.getGender();
			int height = m.getHeight();
			int weight = m.getWeight();
			int disease_id = m.getDiseaseID();
			String phone_number = m.getPhoneNumber();
			String address = m.getAddress();
			int manager = 0;
			// time_stamp = 下面那個;
			int delete_key = 0;

			/**
			 * 將參數回填至SQL指令當中
			 */
			pres = conn.prepareStatement(sql);
			pres.setString(1, id);
			pres.setString(2, name);
			pres.setString(3, email);
			pres.setString(4, password);
			pres.setString(5, bank_account);
			pres.setString(6, birthday);
			pres.setInt(7, gender);
			pres.setInt(8, height);
			pres.setInt(9, weight);
			pres.setInt(10, disease_id);
			pres.setString(11, phone_number);
			pres.setString(12, address);
			pres.setInt(13, manager);
			pres.setInt(14, delete_key);
			/**
			 * 執行新增之SQL指令並記錄影響之行數
			 */
			row = pres.executeUpdate();
			/**
			 * 紀錄真實執行的SQL指令，並印出
			 */
			exexcute_sql = pres.toString();
			System.out.println(exexcute_sql);
		} catch (SQLException e) {
			/**
			 * 印出JDBC SQL指令錯誤
			 */
			System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
		} catch (Exception e) {
			/**
			 * 若錯誤則印出錯誤訊息
			 */
			e.printStackTrace();
		} finally {
			/**
			 * 關閉連線並釋放所有資料庫相關之資源
			 */
			DBMgr.close(pres, conn);
		}
		/**
		 * 紀錄程式結束執行時間
		 */
		long end_time = System.nanoTime();
		/**
		 * 紀錄程式執行時間
		 */
		long duration = (end_time - start_time);
		/** 將SQL指令、花費時間與影響行數，封裝成JSONObject回傳 */
		JSONObject response = new JSONObject();
		response.put("sql", exexcute_sql);
		response.put("time", duration);
		response.put("row", row);
		return response;
	}

	/**
	 * 更新一名會員之會員資料
	 *
	 * @param m 一名會員之Member物件
	 * @return the JSONObject 回傳SQL指令執行結果與執行之資料
	 */
	public JSONObject update(Member m) {
		/**
		 * 紀錄回傳之資料
		 */
		JSONArray jsa = new JSONArray();
		/**
		 * 記錄實際執行之SQL指令
		 */
		String exexcute_sql = "";
		/**
		 * 紀錄程式開始執行時間
		 */
		long start_time = System.nanoTime();
		/**
		 * 紀錄SQL總行數
		 */
		int row = 0;
		try {
			/**
			 * 取得資料庫之連線
			 */
			conn = DBMgr.getConnection();
			/**
			 * SQL指令
			 */
			String sql = " Update `missa`.`member` SET " + "`name` = ? , " + "`email` = ? , " + "`password` = ? , "
					+ "`bank_account` = ? , " + "`birthday` = ? , " + "`gender` = ? , " + "`height` = ? , "
					+ "`weight` = ? , " + "`disease_id` = ? , " + "`phone_number` = ? , " + "`address` = ? , "
					+ "`manager` = ? , " + "`delete_key` = ? " + " WHERE `ID_number` = ? AND `delete_key` = 0 ";
			/**
			 * 取得所需之參數
			 */
			String name = m.getName();
			String email = m.getEmail();
			String password = m.getPassword();
			String bank_account = m.getBankAccount();
			String birthday = m.getBirthday();
			int gender = m.getGender();
			int height = m.getHeight();
			int weight = m.getWeight();
			int disease_id = m.getDiseaseID();
			String phone_number = m.getPhoneNumber();
			String address = m.getAddress();
			int manager = m.getManager();
			int delete_key = m.getDeleteKey();
			String member_id = m.getID();
			/**
			 * 將參數回填至SQL指令當中
			 */
			pres = conn.prepareStatement(sql);
			pres.setString(1, name);
			pres.setString(2, email);
			pres.setString(3, password);
			pres.setString(4, bank_account);
			pres.setString(5, birthday);
			pres.setInt(6, gender);
			pres.setInt(7, height);
			pres.setInt(8, weight);
			pres.setInt(9, disease_id);
			pres.setString(10, phone_number);
			pres.setString(11, address);
			pres.setInt(12, manager);
			pres.setInt(13, delete_key);
			pres.setString(14, member_id);
			/**
			 * 執行更新之SQL指令並記錄影響之行數
			 */
			row = pres.executeUpdate();
			/**
			 * 紀錄真實執行的SQL指令，並印出
			 */
			exexcute_sql = pres.toString();
			System.out.println(exexcute_sql);
		} catch (SQLException e) {
			/**
			 * 印出JDBC SQL指令錯誤
			 */
			System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
		} catch (Exception e) {
			/**
			 * 若錯誤則印出錯誤訊息
			 */
			e.printStackTrace();
		} finally {
			/**
			 * 關閉連線並釋放所有資料庫相關之資源
			 */
			DBMgr.close(pres, conn);
		}
		/**
		 * 紀錄程式結束執行時間
		 */
		long end_time = System.nanoTime();
		/**
		 * 紀錄程式執行時間
		 */
		long duration = (end_time - start_time);
		/**
		 * 將SQL指令、花費時間與影響行數，封裝成JSONObject回傳
		 */
		JSONObject response = new JSONObject();
		response.put("sql", exexcute_sql);
		response.put("row", row);
		response.put("time", duration);
		response.put("data", jsa);
		return response;
	}
	
	public JSONObject checkLogin(String enter_id,String enter_password) {
		/**
		 * 新建一個 Member 物件之 m 變數，用於紀錄每一位查詢回之會員資料
		 */
		Member m = null;
		/**
		 * 用於儲存所有檢索回之會員，以JSONArray方式儲存
		 */
		JSONArray jsa = new JSONArray();
		/**
		 * 記錄實際執行之SQL指令
		 */
		String exexcute_sql = "";
		/**
		 * 紀錄程式開始執行時間
		 */
		long start_time = System.nanoTime();
		/**
		 * 紀錄SQL總行數
		 */
		int row = 0;
		/**
		 * 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料
		 */
		ResultSet rs = null;
		try {
			/**
			 * 取得資料庫之連線
			 */
			conn = DBMgr.getConnection();
			/**
			 * SQL指令
			 */
			String sql = "SELECT * FROM `missa`.`member` WHERE `ID_number` = ? AND `password` = ? AND `delete_key` = 0  LIMIT 1";
			/**
			 * 將參數回填至SQL指令當中
			 */
			pres = conn.prepareStatement(sql);
			pres.setString(1, enter_id);
			pres.setString(2, enter_password);
			/**
			 * 執行查詢之SQL指令並記錄其回傳之資料
			 */
			rs = pres.executeQuery();
			/**
			 * 紀錄真實執行的SQL指令，並印出
			 */
			exexcute_sql = pres.toString();
			System.out.println(exexcute_sql);
			/**
			 * 透過 while 迴圈移動pointer，取得每一筆回傳資料
			 */
			/**
			 * 正確來說資料庫只會有一筆該會員編號之資料，因此其實可以不用使用 while 迴圈
			 */
			while (rs.next()) {
				/**
				 * 每執行一次迴圈表示有一筆資料
				 */
				row += 1;
				/**
				 * 將 ResultSet 之資料取出
				 */
				String ID_number = rs.getString("ID_number");
				String name = rs.getString("name");
				String email = rs.getString("email");
				String password = rs.getString("password");
				String bank_account = rs.getString("bank_account");
				String birthday = rs.getString("birthday");
				int gender = rs.getInt("gender");
				int height = rs.getInt("height");
				int weight = rs.getInt("weight");
				int disease_id = rs.getInt("disease_id");
				String phone_number = rs.getString("phone_number");
				String address = rs.getString("address");
				int manager = rs.getInt("manager");
				int delete_key = rs.getInt("delete_key");
				/**
				 * 將每一筆會員資料產生一名新Member物件
				 */
				m = new Member(ID_number, name, email, password, bank_account, birthday, gender, height, weight, disease_id,
						phone_number, address, manager, delete_key);
				/**
				 * 取出該名會員之資料並封裝至 JSONArray 內
				 */
				jsa.put(m.getData());
			}
		} catch (SQLException e) {
			/**
			 * 印出JDBC SQL指令錯誤
			 */
			System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
		} catch (Exception e) {
			/**
			 * 若錯誤則印出錯誤訊息
			 */
			e.printStackTrace();
		} finally {
			/**
			 * 關閉連線並釋放所有資料庫相關之資源
			 */
			DBMgr.close(rs, pres, conn);
		}
		/**
		 * 紀錄程式結束執行時間
		 */
		long end_time = System.nanoTime();
		/**
		 * 紀錄程式執行時間
		 */
		long duration = (end_time - start_time);
		/**
		 * 將SQL指令、花費時間、影響行數與所有會員資料之JSONArray，封裝成JSONObject回傳
		 */
		JSONObject response = new JSONObject();
		response.put("sql", exexcute_sql);
		response.put("row", row);
		response.put("time", duration);
		response.put("data", jsa);
		return response;
	}

}
