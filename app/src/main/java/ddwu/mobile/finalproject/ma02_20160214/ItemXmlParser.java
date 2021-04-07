package ddwu.mobile.finalproject.ma02_20160214;

import android.text.Html;
import android.text.Spanned;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

public class ItemXmlParser {

    public enum TagType { NONE, TITLE, IMAGE, LPRICE, HPRICE, BRAND, PRODUCTID, CATEGORY1, CATEGORY2 };

    final static String TAG_ITEM = "item";
    final static String TAG_TITLE = "title";
    final static String TAG_IMAGE = "image";
    final static String TAG_LPRICE = "lprice";
    final static String TAG_HPRICE = "hprice";
    final static String TAG_BRAND = "brand";
    final static String TAG_PRODUCTID = "productId";
    final static String TAG_CATEGORY1 = "category1";
    final static String TAG_CATEGORY2 = "category2";

    public ItemXmlParser() {
    }

    public ArrayList<ItemDto> parse(String xml) {

        ArrayList<ItemDto> itemList = new ArrayList();
        ItemDto dto = null;

        TagType tagType = TagType.NONE;

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xml));

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals(TAG_ITEM)) {
                            dto = new ItemDto();
                        } else if (parser.getName().equals(TAG_TITLE)) {
                            if (dto != null) tagType = TagType.TITLE;
                        } else if (parser.getName().equals(TAG_IMAGE)) {
                            if (dto != null) tagType = TagType.IMAGE;
                        } else if (parser.getName().equals(TAG_LPRICE)) {
                            if (dto != null) tagType = TagType.LPRICE;
                        } else if (parser.getName().equals(TAG_HPRICE)) {
                            if (dto != null) tagType = TagType.HPRICE;
                        } else if (parser.getName().equals(TAG_BRAND)) {
                            if (dto != null) tagType = TagType.BRAND;
                        } else if (parser.getName().equals(TAG_PRODUCTID)) {
                            if (dto != null) tagType = TagType.PRODUCTID;
                        } else if (parser.getName().equals(TAG_CATEGORY1)) {
                            if (dto != null) tagType = TagType.CATEGORY1;
                        } else if (parser.getName().equals(TAG_CATEGORY2)) {
                            if (dto != null) tagType = TagType.CATEGORY2;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals(TAG_ITEM)) {
                            if(dto.getCategory1().equals("패션잡화") || dto.getCategory1().equals("패션의류")) {
                                itemList.add(dto);
                                dto = null;
                            }
                            else{
                                dto = null;
                            }
                        }
                        break;
                    case XmlPullParser.TEXT:
                        switch(tagType) {
                            case TITLE:
                                Spanned title = Html.fromHtml(parser.getText());
                                String mtitle = title.toString();
                                dto.setTitle(mtitle);
                                break;
                            case IMAGE:
                                dto.setImageLink(parser.getText());
                                break;
                            case LPRICE:
                                dto.setLprice(parser.getText());
                                break;
                            case HPRICE:
                                if(parser.getText().equals("0")){
                                    dto.setHprice(dto.getLprice());
                                }
                                else {
                                    dto.setHprice(parser.getText());
                                }
                                break;
                            case BRAND:
                                dto.setBrand(parser.getText());
                                break;
                            case PRODUCTID:
                                dto.setProductId(parser.getText());
                                break;
                            case CATEGORY1:
                                dto.setCategory1(parser.getText());
                                break;
                            case CATEGORY2:
                                dto.setCategory2(parser.getText());
                                break;
                        }
                        tagType = TagType.NONE;
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return itemList;
    }

}
