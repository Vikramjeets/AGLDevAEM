package com.aem.community.core.models;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.settings.SlingSettingsService;
import org.apache.commons.io.IOUtils;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This is the model class to consume the json and output a list of all the cats 
 * in alphabetical order under a heading of the gender of their owner.
 *
 * @author Vikramjeet Singh
 *
 */
@Model(adaptables=Resource.class)
public class PetsByOwnerGenderModel {

	@Inject
	private SlingSettingsService settings;

	@Inject @Named("sling:resourceType") @Default(values="No resourceType")
	protected String resourceType;

	ArrayList<String> maleListArray;
	ArrayList<String> femListArray;

	private Map<Integer,List<String>> malemap = new HashMap<Integer, List<String>>();;
	private Map<Integer,List<String>> femalemap = new HashMap<Integer, List<String>>();;



	private final Logger logger = LoggerFactory.getLogger(getClass());

	@PostConstruct
	protected void init() {
		ArrayList<String> maleList = new ArrayList<String>(); 
		ArrayList<String> femList = new ArrayList<String>(); 

		try {        
			String url = "http://agl-developer-test.azurewebsites.net/people.json"; // URL to Parse

			String jsonStr = IOUtils.toString(new URL(url));

			JSONArray jsonarray = new JSONArray(jsonStr);
			for (int i = 0; i < jsonarray.length(); i++) {

				JSONObject obj1 = jsonarray.getJSONObject(i);			
				String gender = obj1.getString("gender");

				if(!obj1.getString("pets").equals("null")) {

					JSONArray petsArray = obj1.getJSONArray("pets");

					for(int j=0; j < petsArray.length();j++){ 

						JSONObject obj2 =  petsArray.getJSONObject(j);

						if(obj2.getString("type").equalsIgnoreCase("Cat")) {
							String petName = obj2.getString("name");
							if(gender.equalsIgnoreCase("male")){
								maleList.add(petName);
							}
							else {
								femList.add(petName);
							}
						}

					}

				}

			}

			maleListArray = sortList(maleList);
			femListArray  = sortList(femList);

		}catch (Exception e) {
			e.printStackTrace();
		}


	}

	/**
	 * This method sorts all the names of the cats 
	 * @return ArrayList<String>
	 */
	public ArrayList<String> sortList(ArrayList<String> al) {  

		/* Collections.sort method is sorting the 
        elements of ArrayList in ascending order. */
		Collections.sort(al);

		return al;

	}


	/**
	 * This method gets list of pets from the json. 
	 * @return ArrayList<String>
	 */
	public List<String> getMales() throws Exception  {

		return  maleListArray;

	}

	/**
	 * This method gets list of pets from the json. 
	 * @return List<ValueMap>
	 */
	public List<String> getFemales() throws Exception  {

		return femListArray;
	}




}