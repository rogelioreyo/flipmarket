package mx.com.agurno.flipmarket;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import mx.com.agurno.flipmarket.entity.Item;
import mx.com.agurno.flipmarket.entity.MarketLog;
import mx.com.agurno.flipmarket.repository.ItemRepository;
import mx.com.agurno.flipmarket.repository.MarketLogRepository;

public class RunFlipMarketTest {

	public static void main(String[] args) throws JsonGenerationException, JsonMappingException, IOException, InvalidKeyException, NoSuchAlgorithmException, InterruptedException, ParseException, org.json.simple.parser.ParseException {

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		AnnotationConfigApplicationContext ctx = IConstantApplicationContext.APPLICATION_CONTEXT;
		ctx.register(Application.class);
		ctx.refresh();
		ItemRepository itemRepository = ((ItemRepository) ctx.getBean("itemRepository"));
		MarketLogRepository marketLogRepository = ((MarketLogRepository) ctx.getBean("marketLogRepository"));
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Market Log");
		Iterable<Item> items = itemRepository.findAll();
		int rowCount = 0;
		Row rowHeaders = sheet.createRow(rowCount);
		for(Item item: items) {
			List<MarketLog> marketLogs = marketLogRepository.findAllByItem(item);
			if(!marketLogs.isEmpty()) {
				int columnCount = 2;
				if(rowCount==0) {
					for(MarketLog marketLog: marketLogs) {
						Cell cellCity = rowHeaders.createCell(++columnCount);
						cellCity.setCellValue(marketLog.getCityName());
					}
				}
				Row row = sheet.createRow(++rowCount);
				columnCount = 2;
				Cell cellItem = row.createCell(columnCount);
				cellItem.setCellValue(item.getItemName());
				Cell cellItemCategory = row.createCell(columnCount-1);
				cellItemCategory.setCellValue(item.getItemCategory().getItemCategoryName());
				Cell cellItemId = row.createCell(columnCount-2);
				cellItemId.setCellValue(item.getItemId());
				for(MarketLog marketLog: marketLogs) {
					try {
						while (!rowHeaders.getCell(columnCount+1).getStringCellValue().equals(marketLog.getCityName())) {
							columnCount++;
						}	
					} catch (Exception e) {
						e.printStackTrace();
					}
					Cell cellSellPriceMin = row.createCell(columnCount+1);
					cellSellPriceMin.setCellValue(marketLog.getSellPriceMin());
				}
			}
		}
		try (FileOutputStream outputStream = new FileOutputStream("C://Temp//MarketLog" + simpleDateFormat.format(new Date()) + ".xlsx")) {
			workbook.write(outputStream);
		}
	}
	
}
