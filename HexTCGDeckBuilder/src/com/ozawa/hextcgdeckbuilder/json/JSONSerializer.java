/*******************************************************************************
 * Hex TCG Deck Builder
 *     Copyright ( C ) 2014  Chad Kinsella, Dave Kerr and Laurence Reading
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.ozawa.hextcgdeckbuilder.json;

import com.ozawa.hextcgdeckbuilder.enums.Attribute;
import com.ozawa.hextcgdeckbuilder.hexentities.Card;
import com.ozawa.hextcgdeckbuilder.hexentities.HexDeck;
import com.ozawa.hextcgdeckbuilder.enums.CardType;
import com.ozawa.hextcgdeckbuilder.enums.ColorFlag;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Class used to serialize and deserialize JSON to required Hex entities and vice versa.
 */
public class JSONSerializer {
	
	/**
	 * Deserialize a JSON String into a Card
	 * 
	 * @param json
	 * @return A Card deserialized from the given JSON
	 */
	public static Card deserializeJSONtoCard(String json){
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Attribute[].class, new MultiValueSerializer<Attribute>(Attribute.class));
		gsonBuilder.registerTypeAdapter(ColorFlag[].class, new MultiValueSerializer<ColorFlag>(ColorFlag.class));
		gsonBuilder.registerTypeAdapter(CardType[].class, new MultiValueSerializer<CardType>(CardType.class));
		gsonBuilder.registerTypeAdapter(Boolean.class, new BooleanSerializer());
		Gson gson = gsonBuilder.create();
		
		Card newCard = gson.fromJson(json, Card.class);
		
		return newCard;
	}
	
	/**
	 * Deserialize a JSON String into a Deck
	 * 
	 * @param json
	 * @return A Deck deserialized from the given JSON
	 */
	public static HexDeck deserializeJSONtoDeck(String json){
		Gson gson = new Gson();
		
		HexDeck newDeck = gson.fromJson(json, HexDeck.class);
		
		return newDeck;
	}
	
}
