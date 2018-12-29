package Game;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import Search.Expectiminimax;

public class WriteExcel {
	public String agentType;
	public int oneMaxTile;
	public int oneScore;
	public int maxDepth;
	public int delayExpectiminimax;
	public ArrayList<Integer> allScore = new ArrayList<Integer>();
	public ArrayList<Double> allAverageTileEnd = new ArrayList<Double>();
	public ArrayList<Integer> allMaxTile = new ArrayList<Integer>();
	public ArrayList<Long> allTime = new ArrayList<Long>();
	public HSSFWorkbook workbook = new HSSFWorkbook();
	public HSSFSheet sheet;
	public HSSFRow row;
	public HSSFCell cell;
	public int rowIndex = 0;
	
	public WriteExcel() {
		//Intialise the excel document
		workbook = new HSSFWorkbook();
		//this.agentType = type;
		sheet = workbook.createSheet("Sheet");
		row = sheet.createRow(0);
		cell = row.createCell(0);
		cell.setCellValue("Game Number");
		cell = row.createCell(1);
		cell.setCellValue("the Maximum Tile in the Game");
		cell = row.createCell(2);
		cell.setCellValue("Score at the end of the Game");
		cell = row.createCell(3);
		cell.setCellValue("Average value of the Tiles at the End");
		cell = row.createCell(4);
		cell.setCellValue("Duration of the Game (ns)");
		cell = row.createCell(5);
		cell.setCellValue("Duration of the Game (Formatted) (min)");
		
	}
	public  void receiveOneGameStats(int mT, int mS, double aT, long time) {
		oneMaxTile = mT;
		oneScore = mS;
		allScore.add(mS);
		allMaxTile.add(mT);
		allAverageTileEnd.add(aT);
		allTime.add(time);
		row = sheet.createRow(rowIndex+1);
		//First Column 
		cell = row.createCell(0);
		cell.setCellValue(rowIndex+1);
		//Second Column
		cell = row.createCell(1);
		cell.setCellValue(allMaxTile.get(rowIndex));
		//Third Column
		cell = row.createCell(2);
		cell.setCellValue(allScore.get(rowIndex));
		//Fourth Column 
		cell = row.createCell(3);
		cell.setCellValue(allAverageTileEnd.get(rowIndex));
		//Fifth Column
		cell = row.createCell(4);
		cell.setCellValue(allTime.get(rowIndex));
		//Sixth Column
		cell = row.createCell(5);
		cell.setCellValue(GameBoard.formatTime(allTime.get(rowIndex) / (6*10^10)));
		rowIndex++;
		if ((rowIndex % 5 == 0)) {
			try {
				workbook.write(new FileOutputStream(agentType+" backup"+ rowIndex +" " + Integer.toString(Expectiminimax.delay)+".xls"));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public  void printResult() throws IOException{
		/*for (int i = 0; i < allScore.size();i++) {
			row = sheet.createRow(i+1);
			//First Column 
			cell = row.createCell(0);
			cell.setCellValue(i+1);
			//Second Column
			cell = row.createCell(1);
			cell.setCellValue(allMaxTile.get(i));
			//Third Column
			cell = row.createCell(2);
			cell.setCellValue(allScore.get(i));
			//Fourth Column 
			cell = row.createCell(3);
			cell.setCellValue(allAverageTileEnd.get(i));
			//Fifth Column
			cell = row.createCell(4);
			cell.setCellValue(allTime.get(i));
			//Sixth Column
			cell = row.createCell(5);
			cell.setCellValue(GameBoard.formatTime(allTime.get(i) / 1000));
		}*/
		if (agentType.equals("Search.Expectiminimax")) {
			row = sheet.createRow(2+allScore.size());
			cell = row.createCell(0);
			//First Column
			cell.setCellValue("Each Move took");
			cell = row.createCell(1);
			cell.setCellValue(delayExpectiminimax);
		}
		
		workbook.write(new FileOutputStream(agentType+" "+Integer.toString(delayExpectiminimax)+".xls"));
		workbook.close();
	}
	public void setType(String name) {
		agentType = name;
	}
	public  void setDelay(int delay) {
		delayExpectiminimax = delay;
	}
}
