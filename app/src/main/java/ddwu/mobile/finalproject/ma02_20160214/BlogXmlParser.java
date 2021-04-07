package ddwu.mobile.finalproject.ma02_20160214;

import android.text.Html;
import android.text.Spanned;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

public class BlogXmlParser {

    public enum TagType { NONE, TITLE, LINK, DESCRIPTION, BLOGGERNAME };

    final static String TAG_ITEM = "item";
    final static String TAG_TITLE = "title";
    final static String TAG_LINK = "link";
    final static String TAG_DESCRIPTION = "description";
    final static String TAG_BLOGGERNAME = "bloggername";

    public BlogXmlParser() {
    }

    public ArrayList<BlogDto> parse(String xml) {

        ArrayList<BlogDto> blogList = new ArrayList();
        BlogDto dto = null;

        BlogXmlParser.TagType tagType = BlogXmlParser.TagType.NONE;

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
                            dto = new BlogDto();
                        } else if (parser.getName().equals(TAG_TITLE)) {
                            if (dto != null) tagType = TagType.TITLE;
                        } else if (parser.getName().equals(TAG_LINK)) {
                            if (dto != null) tagType = TagType.LINK;
                        } else if (parser.getName().equals(TAG_DESCRIPTION)) {
                            if (dto != null) tagType = TagType.DESCRIPTION;
                        } else if (parser.getName().equals(TAG_BLOGGERNAME)) {
                            if (dto != null) tagType = TagType.BLOGGERNAME;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals(TAG_ITEM)) {
                            blogList.add(dto);
                            dto = null;
                        }
                        break;
                    case XmlPullParser.TEXT:
                        switch(tagType) {
                            case TITLE:
                                Spanned title = Html.fromHtml(parser.getText());
                                String mtitle = title.toString();
                                dto.setTitle(mtitle);
                                break;
                            case LINK:
                                dto.setLink(parser.getText());
                                break;
                            case DESCRIPTION:
                                Spanned des = Html.fromHtml(parser.getText());
                                String mdes = des.toString();
                                dto.setDescription(mdes);
                                break;
                            case BLOGGERNAME:
                                dto.setBloggername(parser.getText());
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

        return blogList;
    }
}
