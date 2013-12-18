package com.ozawa.android.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ozawa.android.enums.Attribute;
import com.ozawa.android.enums.CardType;
import com.ozawa.android.enums.ColorFlag;
import com.ozawa.android.hexentities.Card;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lreading on 18/12/13.
 */
public class JsonReader {

    Gson gson;

    public JsonReader(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Attribute[].class, new MultiValueSerializer<Attribute>(Attribute.class));
        gsonBuilder.registerTypeAdapter(ColorFlag[].class, new MultiValueSerializer<ColorFlag>(ColorFlag.class));
        gsonBuilder.registerTypeAdapter(CardType[].class, new MultiValueSerializer<CardType>(CardType.class));
        gsonBuilder.registerTypeAdapter(Boolean.class, new BooleanSerializer());
        gson = gsonBuilder.create();
    }

    public Card deserializeJSONInputStreamToCard(InputStream jsonStream){
        InputStreamReader reader = new InputStreamReader(jsonStream);
        Card newCard = gson.fromJson(reader,Card.class);
        return newCard;
    }

    public List<Card> deserializeJSONInputStreamsToCard(InputStream[] jsonStreams){
        Card[] newCards = new Card[jsonStreams.length];
        for(int i = 0;i<jsonStreams.length;i++){
            newCards[i]=deserializeJSONInputStreamToCard(jsonStreams[i]);
        }
        return Arrays.asList(newCards);
    }

}