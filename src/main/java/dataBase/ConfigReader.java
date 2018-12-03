package dataBase;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * config reader used to read and return the value of the config
 * @author yalei
 *
 */
public class ConfigReader {
	private JsonObject object;
	
	/**
	 * construct the config reader by reading the config file
	 * @param input
	 */
	public ConfigReader(String input) {
		JsonParser jp = new JsonParser();
		try(BufferedReader reader = new BufferedReader(new FileReader(input))){
			String file = reader.readLine();
			JsonElement e = jp.parse(file);
			this.object = (JsonObject) e;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * get the values as a String
	 * @param key
	 * @return
	 */
	public String getConfig(String key) {
		return object.get(key).getAsString();
	}

}
