package labs.sdm.practica.parser;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import labs.sdm.practica.pojo.Question;

public class QuestionXmlParser {

    public List<Question> parseQuestion(InputStream inputStream) throws XmlPullParserException, IOException {
//        Log.d("QuestionXmlParser", "XML Parser");
        XmlPullParser parser = Xml.newPullParser();

        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(inputStream,null);

        List<Question> questions = null;

        int eventType = parser.getEventType();

        Question currentQuestion = null;
        while(eventType != XmlPullParser.END_DOCUMENT){
            String name = null;
            switch (eventType){
                case XmlPullParser.START_DOCUMENT:
                    questions = new ArrayList<>();
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    if(name.equals("question")){
                        currentQuestion = new Question();
                        currentQuestion.setAnswer1(parser.getAttributeValue(null, "answer1"));
                        currentQuestion.setAnswer2(parser.getAttributeValue(null, "answer2"));
                        currentQuestion.setAnswer3(parser.getAttributeValue(null, "answer3"));
                        currentQuestion.setAnswer4(parser.getAttributeValue(null, "answer4"));
                        currentQuestion.setAudience(parser.getAttributeValue(null, "audience"));
                        currentQuestion.setFifty1(parser.getAttributeValue(null, "fifty1"));
                        currentQuestion.setFifty2(parser.getAttributeValue(null, "fifty2"));
                        currentQuestion.setNumber(parser.getAttributeValue(null, "number"));
                        currentQuestion.setPhone(parser.getAttributeValue(null, "phone"));
                        currentQuestion.setRight(parser.getAttributeValue(null, "right"));
                        currentQuestion.setText(parser.getAttributeValue(null, "text"));
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                    if (name.equals("question") && currentQuestion != null){
                        questions.add(currentQuestion);
                    }
            }
            eventType = parser.next();
        }

        return questions;
    }

}