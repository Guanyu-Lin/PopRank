import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Pre_Rec {
	static Record result[];
	private double correct[];
	private Map<Integer, Map<Integer, Double>> I_u_te;
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Pre_Rec program = new Pre_Rec();
		program.readIn();
		program.calAndSaveError();
	
	}
	Pre_Rec() {
		I_u_te = new HashMap<Integer, Map<Integer,Double>>();
		correct = new double [Data.USER_NUM + 1];
	}
	void calAndSaveError() throws IOException {
		File f = new File(Data.ERROR_PATH);
		if (!f.exists()) f.createNewFile();		
		FileWriter fw = new FileWriter(f);
		BufferedWriter save = new BufferedWriter(fw);
		String data;
		int u,i;
		for (Record tmp : result) {
			u = tmp.userID;
			i = tmp.itemID;
			if (I_u_te.get(u).containsKey(i)) {
				correct[u] ++;
			}
		}
		double preSum = 0;
		double recSum = 0;
		for (u = 1; u <= Data.USER_NUM; u++) {
			if (correct[u] != 0) {
				preSum += correct[u] / Data.K;
				recSum += correct[u] / I_u_te.get(u).size();
			}
		}
		double pre = preSum / I_u_te.size();
		double rec = recSum / I_u_te.size();
		data = "Pre@K : " + pre + '\t' + "Rec@K : " + rec + '\n';
		save.write(data);
		
		save.flush();
		save.close();
	
	}
	void readIn() throws IOException {
	
		File file = new File(Data.TEST_PATH);
		FileReader fileReader = new FileReader(file);
		
		for (int i = 0; i < Data.TEST_RECORD_NUM; i++) {
			int user = readInt(fileReader);
			int item = readInt(fileReader);
			double r_ui = readInt(fileReader);
			readInt(fileReader);
			if (!I_u_te.containsKey(user)) {
				I_u_te.put(user, new HashMap<Integer, Double>());
			}
			I_u_te.get(user).put(item, r_ui);
		}
		
		fileReader.close();
		
		result = new Record[Data.RESULT_NUM];
		
		File ucfFile = new File(Data.PRE_RESULT_PATH)  ;
		FileInputStream ucfFileIn = new FileInputStream(ucfFile);
		InputStreamReader ucfIn = new InputStreamReader(ucfFileIn);
		BufferedReader ucfReader =  new BufferedReader(ucfIn);
		String data[];
		String line;
		int index = 0;
		while((line = ucfReader.readLine()) != null) {
			data = line.split("\\s+");
			result[index++] = new Record(Integer.valueOf(data[0]), Integer.valueOf(data[1]), Double.valueOf(data[2]));
			
		}
		ucfReader.close();
		ucfIn.close();
		ucfFileIn.close();
		
	}
	private int readInt(FileReader fileReader) throws IOException {
		int ret = 0;
		while (true) {
			char c = (char)fileReader.read();
			if ((int)c == -1 || !Character.isDigit(c)) {
				break;
			}
			
			ret *= 10;
			ret += (c - '0');
		}
		return ret;
	}
	
}
