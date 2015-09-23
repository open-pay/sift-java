/**
 * 
 */
package com.mcac0006.siftscience;

import java.io.IOException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.mcac0006.siftscience.event.domain.Event;
import com.mcac0006.siftscience.exception.SiftScienceException;
import com.mcac0006.siftscience.label.domain.Label;
import com.mcac0006.siftscience.partner.domain.SiftMerchant;
import com.mcac0006.siftscience.result.domain.SiftMerchantResponse;
import com.mcac0006.siftscience.result.domain.SiftScienceResponse;
import com.mcac0006.siftscience.score.domain.SiftScienceScore;

/**
 * This helper will take care of marshalling the content you wish to send to Sift Science and 
 * also POST send it to Sift Science.
 * 
 * <strong>This class is synchronous.</strong>
 * 
 * @author <a href="mailto:matthew.cachia@gmail.com">Matthew Cachia</a>
 *
 */
public class SiftScienceHelper {
	
	private static ObjectMapper mapper;
	private static String PATH_EVENTS_API = "https://api.siftscience.com/v203/events";
	private static String PATH_SCORE_API = "https://api.siftscience.com/v203/score/";
	private static String PATH_LABELS_API = "https://api.siftscience.com/v203/users/";
	private static String PATH_PARTNERS_API = "https://api3.siftscience.com/v3/partners/";	
	private static String PATH_DEVICE_FINGERPRINTING_API = "https://api3.siftscience.com/v3/accounts/";
	
	static {
		mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Inclusion.NON_NULL);
	}

	private SiftScienceHelper() {}
	
	/**
	 * Sends an event ($transaction, $create_account, etc ...) to Sift Science.
	 * 
	 * @param event - the content regarding the user (or session) in question.
	 * @return the Sift Science response which denotes whether the request has been processed successfully or not.
	 */
	public static SiftScienceResponse send(final Event event) {
		
		try {
			
			final Client client = ClientBuilder.newClient();
			final WebTarget target = client.target(PATH_EVENTS_API);
			final Builder request = target.request(MediaType.APPLICATION_JSON_TYPE);
			final Response post = request.post(Entity.entity(serialize(event), MediaType.APPLICATION_JSON_TYPE));
			
			final SiftScienceResponse siftResult = deserializeResponse(post.readEntity(String.class));
			return siftResult;
			
		} catch (IOException e) {
			throw new SiftScienceException("Error generating JSON content to send event.", e);
		}
	}
	
	/**
	 * Sends a Label ($label) to Sift Science.
	 * 
	 * @param userId - the user in question
	 * @param label - the content regarding the user in question.
	 * @return the Sift Science response which denotes whether the request has been processed successfully or not.
	 */
	public static SiftScienceResponse send(final String userId, final Label label) {
		
		try {
			
			final Client client = ClientBuilder.newClient();
			final WebTarget target = client.target(PATH_LABELS_API).path(userId).path("labels");
			final Builder request = target.request(MediaType.APPLICATION_JSON_TYPE);
			final Response post = request.post(Entity.entity(serialize(label), MediaType.APPLICATION_JSON_TYPE));
			
			final SiftScienceResponse siftResult = deserializeResponse(post.readEntity(String.class));
			return siftResult;
			
		} catch (IOException e) {
			throw new SiftScienceException("Error generating JSON content to send label.", e);
		}
	}

	/**
	 * Sends a create account request ($accounts) to Sift Science.
	 * 
	 * @param partnerId - the partner in question
	 * @param apikey - the api key to denote which Sift Science account to use.
	 * @param merchantAccount - the entity of Sift Science merchant account to create.

	 * @return the Sift Science response with the account created.
	 */
	public static SiftMerchantResponse createAccount(final String partnerId, final String apikey, final SiftMerchant merchantAccount) {
		
		try {
			
			final Client client = ClientBuilder.newClient().register(new Authenticator(apikey,""));
			final WebTarget target = client.target(PATH_PARTNERS_API).path(partnerId).path("/accounts");
			final Builder request = target.request(MediaType.APPLICATION_JSON_TYPE);
			final Response post = request.post(Entity.entity(serialize(merchantAccount), MediaType.APPLICATION_JSON_TYPE));
			
			final SiftMerchantResponse siftMerchantResult = deserializeMerchantResponse(post.readEntity(String.class));
			return siftMerchantResult;
			
		} catch (IOException e) {
			throw new SiftScienceException("Error generating JSON content to createAccount " + e.getMessage(), e);
		}
	}

	public static String listAccounts(final String partnerId, final String apikey) {
		
			final Client client = ClientBuilder.newClient().register(new Authenticator(apikey,""));
			final WebTarget target = client.target(PATH_PARTNERS_API).path(partnerId).path("/accounts");
			final Builder request = target.request(MediaType.APPLICATION_JSON_TYPE);
			final Response get = request.get();
			
			return get.readEntity(String.class);
			
	}
	
	/**
	 * Sends a getSession request to Sift Science.
	 * 
	 * @param accountId - the sift account in question
	 * @param apikey - the api key to denote which Sift Science account to use.
	 * @param sessionId - the sessionId of your server session.

	 * @return the Sift Science response with the device info of the session.
	 */
	public static String getSession( final String accountId, final String apikey, String sessionId ){
		
		final Client client = ClientBuilder.newClient().register(new Authenticator(apikey,""));
		final WebTarget target = client.target(PATH_DEVICE_FINGERPRINTING_API).path(accountId).path("/sessions").path(sessionId);
		final Builder request = target.request(MediaType.APPLICATION_JSON_TYPE);
		final Response get = request.get();
		
		return get.readEntity(String.class);
	}

	/**
	 * Retrieve a risk assessment of a particular user. This is particularly useful to consult with Sift Science 
	 * before you proceed with any (user-invoked or system-invoked) operations (such as a purchase) on that user.
	 * 
	 * @param api_key - the api key to denote which Sift Science account to use.
	 * @param userId - the user would you like to run a risk assessment on.
	 * 
	 * @return a Sift Science score wrapped in a {@link SiftScienceScore} instance containing information such as the 
	 *         fraud score and the reason. 
	 *         
	 *         Refer to the class' JavaDocs for more information.
	 */
	public static SiftScienceScore getScore(final String api_key, final String userId) {
		
		try {
			
			final Client client = ClientBuilder.newClient();
			final WebTarget target = client.target(PATH_SCORE_API).path(userId).queryParam("api_key", api_key);
			final Builder request = target.request(MediaType.APPLICATION_JSON_TYPE);
			final Response get = request.get();
			
			final SiftScienceScore score = deserializeScore(get.readEntity(String.class));
			return score;
			
		} catch (IOException e) {
			throw new RuntimeException("Error generating JSON content to retrieve score request.", e);
		}
		
	}

	/**
	 * <p>Serializes an event as a JSON envelope, ready to be sent to Sift Science.</p>
	 * 
	 * <p><strong>Useful if you have your own way of sending the envelope.</strong> Should you require a way of 
	 * sending the envelope via Http, you can make use of {@link SiftScienceHelper#send(Event)}.</p>
	 * 
	 * @param event the filled event POJO, ready to be serialized
	 * @return the event in JSON form.
	 * @throws IOException thrown whenever an error (unexpected or user-inflicted) has been found during serialization of the event. 
	 */
	public static String serialize(final Event event) throws IOException {
		return mapper.writeValueAsString(event);
	}
	
	public static String serialize(final Label label) throws IOException {
		return mapper.writeValueAsString(label);
	}

    private static String serialize(final SiftMerchant merchantAccount) throws IOException {
    	System.out.println("serialization:"+mapper.writeValueAsString(merchantAccount)); 
        return mapper.writeValueAsString(merchantAccount);
    }

	/**
	 * <p>Deserializes a response after sending an {@link Event} or a {@link Label}.
	 * 
	 * @param $response the JSON envelope withholding Sift Science's response.
	 * @return the response in POJO.
	 * @throws IOException thrown whenever an error (unexpected or user-inflicted) has been found during deserialization of the event or label.
	 */
	public static SiftScienceResponse deserializeResponse(final String $response) throws IOException {
		return mapper.readValue($response, SiftScienceResponse.class);
	}
	
	/**
	 * <p>Deserializes the score returned by Sift Science.</p>
	 * 
	 * <p><strong>Useful if you have your own way of sending and receiving requests.</strong> Should you require a way of 
	 * receiving the score Http, you can make use of {@link SiftScienceHelper#getScore(String, String)}.</p>
	 * 
	 * @param $scoreResponse the JSON envelope withholding Sift Science's response.
	 * @return the response in POJO.
	 * @throws IOException thrown whenever an error (unexpected or user-inflicted) has been found during deserialization of the score.
	 */
	public static SiftScienceScore deserializeScore(final String $scoreResponse) throws IOException {
		return mapper.readValue($scoreResponse, SiftScienceScore.class);
	}
	
	/**
	 * <p>Deserializes the account returned by Sift Science.</p>
	 * 
	 * @param $$accountResponse the JSON envelope withholding Sift Science's response.
	 * @return the response in POJO.
	 * @throws IOException thrown whenever an error (unexpected or user-inflicted) has been found during deserialization of the account returned.
	 */
	private static SiftMerchantResponse deserializeMerchantResponse(final String accountResponse) throws IOException  {
    	try {
			return mapper.readValue(accountResponse, SiftMerchantResponse.class);
		} catch (JsonParseException e) {
			throw new IOException("response error: " + accountResponse, e);
		} catch (JsonMappingException e) {
			throw new IOException("response error: " + accountResponse, e);
		} 
    }

}