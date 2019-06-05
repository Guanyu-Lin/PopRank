import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class PopRank {
	private double ave_r;
	private double r_u_i[][];
	private Map<Integer, Map<Integer, Double>> record_i;
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		PopRank program = new PopRank();
		program.initial();
		program.savePreditedRating();
	}
	PopRank() {
		this.ave_r = Data.TRAINNING_RECORD_NUM / (Data.ITEM_NUM * Data.USER_NUM);
		record_i = new HashMap<Integer, Map<Integer,Double>>();
		r_u_i = new double[Data.USER_NUM + 1][Data.ITEM_NUM + 1];
	}
	private void initial() throws IOException {
		
		
		File ucfFile = new File(Data.BASE_PATH)  ;
		FileInputStream ucfFileIn = new FileInputStream(ucfFile);
		InputStreamReader ucfIn = new InputStreamReader(ucfFileIn);
		BufferedReader ucfReader =  new BufferedReader(ucfIn);
		String data[];
		String line;
		
		while((line = ucfReader.readLine()) != null) {
			data = line.split("\\s+");
			int user = Integer.valueOf(data[0]);
			int item = Integer.valueOf(data[1]);
			double r_ui = Double.valueOf(data[2]);
			
			r_u_i[user][item] = r_ui;
			
			if (!record_i.containsKey(item)) {
				record_i.put(item, new HashMap<Integer, Double>());
			}
			record_i.get(item).put(user, r_ui);
			
		}
		ucfReader.close();
		ucfIn.close();
		ucfFileIn.close();
		
		
	}
/*	private int readInt(FileReader fileReader) throws IOException {
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
	}*/
	void savePreditedRating() throws IOException {
		File f = new File(Data.PRE_RESULT_PATH);
		if (!f.exists()) f.createNewFile();
		
		FileWriter fw = new FileWriter(f);
		BufferedWriter save = new BufferedWriter(fw);
		double result;
		for (int u = 1; u <= Data.USER_NUM; u++) {
			for (int i = 1; i <= Data.ITEM_NUM; i++) {
				if (r_u_i[u][i] == 0) {
					if (record_i.containsKey(i)) {
						result = record_i.get(i).size() / Data.USER_NUM - this.ave_r;
						System.out.println( record_i.get(i).size());
					}
						
					else  result = -this.ave_r;
					//if (result >= 4)
					save.write(u + " " + i + " " + result + '\n');
				}
				
				
			}
		
		}
		save.flush();
		save.close();
	}
}
