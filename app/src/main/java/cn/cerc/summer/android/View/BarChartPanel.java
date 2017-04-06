
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

//Öù×´Í¼»æÖÆÀà
public class BarChartPanel extends  View{
	
	private String plotTitle;
	private DataSeries series;
	public final static int[] platterTable = new int[]{Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.CYAN};
	public BarChartPanel(Context context, String plotTitle) {
		this(context);
		this.plotTitle = plotTitle;
	}
	
	public BarChartPanel(Context context) {
		super(context);
	}
	
	public void setSeries(DataSeries series) {
		this.series = series;
	}
	
	@Override  
    public void onDraw(Canvas canvas) { 
		
		// get default screen size from system service
		WindowManager wm = (WindowManager) this.getContext().getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		int width = display.getWidth();
		
		// remove application title height
		int height = display.getHeight() - 80; 
		System.out.println("width = " + width);
		System.out.println("height = " + height);
		
		// draw background
		Paint myPaint = new Paint();
		myPaint.setColor(Color.BLUE);
		myPaint.setStrokeWidth(2);
		canvas.drawRect(0, 0, width, height, myPaint);
		myPaint.setColor(Color.WHITE);
		myPaint.setStrokeWidth(0);
		canvas.drawRect(2, 2, width-2, height-2, myPaint);
		
		// draw XY Axis
		int xOffset = (int)(width * 0.1);
		int yOffset = (int)(height * 0.1);
		System.out.println("xOffset = " + xOffset);
		
		myPaint.setColor(Color.BLACK);
		myPaint.setStrokeWidth(2);
		canvas.drawLine(2+xOffset, height-2-yOffset, 2+xOffset, 2, myPaint);
		canvas.drawLine(2+xOffset, height-2-yOffset, width-2, height-2-yOffset, myPaint);
		
		// draw text title
		myPaint.setAntiAlias(true);
		myPaint.setStyle(Style.FILL);
		canvas.drawText(plotTitle, (width-2)/4, 30, myPaint);
		
		// draw data series now......
		if(series == null) {
			getMockUpSeries();
		}
		int xPadding = 10;
		if(series != null) {
		int count = series.getSeriesCount();
		int xUnit = (width - 2 - xOffset)/count;
			String[] seriesNames = series.getSeriesKeys();
			for(int i=0; i<seriesNames.length; i++) {
				canvas.drawText(seriesNames[i], xOffset + 2 + xPadding + xUnit*i, height-yOffset + 10, myPaint);
			}
			
			// Y Axis markers
			float min = 0, max = 0;
			for(int i=0; i<seriesNames.length; i++) {
				List<DataElement> itemList = series.getItems(seriesNames[i]);
				if(itemList != null && itemList.size() > 0) {
					for(DataElement item : itemList) {
						if(item.getValue() > max) {
							max = item.getValue();
						}
						if(item.getValue() < min) {
							min = item.getValue();
						}
					}
				}
			}
			
			int yUnit = 22;	
			int unitValue = (height-2-yOffset)/yUnit;
			myPaint.setStyle(Style.STROKE);
			myPaint.setStrokeWidth(1);
			myPaint.setColor(Color.LTGRAY);
			myPaint.setPathEffect(new DashPathEffect(new float[] {1,3}, 0));
			float ymarkers = (max-min)/yUnit;
			NumberFormat nf = NumberFormat.getInstance();
			nf.setMinimumFractionDigits(2);
			nf.setMaximumFractionDigits(2);
			for(int i=0; i<20; i++) {
				canvas.drawLine(2+xOffset, height-2-yOffset - (unitValue * (i+1)), width-2, height-2-yOffset - (unitValue * (i+1)), myPaint);
			}
			
			// clear the path effect
			myPaint.setColor(Color.BLACK);
			myPaint.setStyle(Style.STROKE);
			myPaint.setStrokeWidth(0);
			myPaint.setPathEffect(null); 
			for(int i=0; i<20; i++) {
				float markValue = ymarkers * (i+1);
				canvas.drawText(nf.format(markValue), 3, height-2-yOffset - (unitValue * (i+1)), myPaint);
			}
			
			// draw bar chart now
			myPaint.setStyle(Style.FILL);
			myPaint.setStrokeWidth(0);
			String maxItemsKey = null;
			int maxItem = 0;
			for(int i=0; i<seriesNames.length; i++) {
				List<DataElement> itemList = series.getItems(seriesNames[i]);
				int barWidth = (int)(xUnit/Math.pow(itemList.size(),2));
				int startPos = xOffset + 2 + xPadding + xUnit*i;
				int index = 0;
				int interval = barWidth/2;
				if(itemList.size() > maxItem) {
					maxItemsKey = seriesNames[i];
					maxItem = itemList.size();
				}
				for(DataElement item : itemList) {
					myPaint.setColor(item.getColor());
					int barHeight = (int)((item.getValue()/ymarkers) * unitValue);
					canvas.drawRect(startPos + barWidth*index + interval*index, height-2-yOffset-barHeight, 
							startPos + barWidth*index + interval*index + barWidth, height-2-yOffset, myPaint);
					index++;
				}
			}
			
			List<DataElement> maxItemList = series.getItems(maxItemsKey);
			int itemIndex = 0;
			int basePos = 10;
			for(DataElement item : maxItemList) {
				myPaint.setColor(item.getColor());
				canvas.drawRect(basePos + itemIndex * 10, height-yOffset + 15, basePos + itemIndex * 10 + 10, height-yOffset + 30, myPaint);
				myPaint.setColor(Color.BLACK);
				canvas.drawText(item.getItemName(), basePos + (itemIndex+1) * 10, height-yOffset + 25, myPaint);
				itemIndex++;
				basePos = basePos + xUnit*itemIndex;
			}
		}
	}
	
    
    public DataSeries getMockUpSeries() {
    	series = new DataSeries();
    	List<DataElement> itemListOne = new ArrayList<DataElement>();
    	itemListOne.add(new DataElement("shoes",120.0f, platterTable[0]));
    	itemListOne.add(new DataElement("jacket",100.0f, platterTable[1]));
    	series.addSeries("First Quarter", itemListOne);
    	
    	List<DataElement> itemListTwo = new ArrayList<DataElement>();
    	itemListTwo.add(new DataElement("shoes",110.0f, platterTable[0]));
    	itemListTwo.add(new DataElement("jacket",50.0f, platterTable[1]));
    	series.addSeries("Second Quarter", itemListTwo);
    	
    	List<DataElement> itemListThree = new ArrayList<DataElement>();
    	itemListThree.add(new DataElement("shoes",100.0f, platterTable[0]));
    	itemListThree.add(new DataElement("jacket",280.0f, platterTable[1]));
    	series.addSeries("Third Quarter", itemListThree);
    	
    	List<DataElement> itemListFour = new ArrayList<DataElement>();
    	itemListFour.add(new DataElement("shoes",120.0f, platterTable[0]));
    	itemListFour.add(new DataElement("jacket",100.0f, platterTable[1]));
    	series.addSeries("Fourth Quarter", itemListFour);
    	return series;
    }

}