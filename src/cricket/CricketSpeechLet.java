package cricket;

import org.slf4j.LoggerFactory;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.speechlet.SpeechletV2;
import com.amazon.speech.ui.OutputSpeech;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SsmlOutputSpeech;

public class CricketSpeechlet implements SpeechletV2 {

	private static org.slf4j.Logger log = LoggerFactory.getLogger(CricketSpeechlet.class);
	
	@Override
	public void onSessionStarted(SpeechletRequestEnvelope<SessionStartedRequest> requestEnvelope) {
		log.info("onSessionStarted requestId = {} sessionId = {}", requestEnvelope.getRequest().getRequestId(),
				requestEnvelope.getSession().getSessionId());
		
	}

	@Override
	public SpeechletResponse onLaunch(SpeechletRequestEnvelope<LaunchRequest> requestEnvelope) {
		log.info("onSessionStarted requestId = {} sessionId = {}", requestEnvelope.getRequest().getRequestId(),
				requestEnvelope.getSession().getSessionId());
		return getWelcomeMessage();
	}

	@Override
	public SpeechletResponse onIntent(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
		IntentRequest request = requestEnvelope.getRequest();
		log.info("onSessionStarted requestId = {} sessionId = {}", request.getRequestId(),
				requestEnvelope.getSession().getSessionId());
		
		Intent intent = request.getIntent();
		String intentName = (intent != null) ? intent.getName() : null;
		if ("AMAZON.StopIntent".equals(intentName)) {
			return getGoodbyeMessage();
		} else if ("AMAZON.CancelIntent".equals(intentName)) {
			return getGoodbyeMessage();
		} else if ("AMAZON.HelpIntent".equals(intentName)) {
			return getHelpMessage();
		} else {
			return handleGameStartIntent("");
		}
	}

	@Override
	public void onSessionEnded(SpeechletRequestEnvelope<SessionEndedRequest> requestEnvelope) {
		log.info("onSessionStarted requestId = {} sessionId = {}", requestEnvelope.getRequest().getRequestId(),
				requestEnvelope.getSession().getSessionId());
		
	}
	
	private SpeechletResponse getGoodbyeMessage() {
		String speechText = "Goodbye";
		return getNewTellResponse(speechText);
	}
	
	private SpeechletResponse getHelpMessage() {
		String speechText = "Do you think you can pick between two difficult choices? Try saying 'play my choice cricket' to test yourself.";
		return getNewTellResponse(speechText);
	}
	
	private SpeechletResponse handleGameStartIntent(String welcomeMessage) {
		String speechText = welcomeMessage;
		speechText += QuestionFactory.getQuestion();
		String repromptText = "You can say 'Skip' to skip this question, or 'Stop' exit.";
		return getNewAskResponse(speechText, repromptText);
	}
	
	private SpeechletResponse getWelcomeMessage() {
		String speechText = IntroMessageFactory.getIntroMessage();
		speechText += "Welcome! ";
		return handleGameStartIntent(speechText);
	}
	
	private SpeechletResponse getNewTellResponse(String speechText) {
		OutputSpeech outputSpeech;
		outputSpeech = new PlainTextOutputSpeech();
		((PlainTextOutputSpeech) outputSpeech).setText(speechText);
		
		return SpeechletResponse.newTellResponse(outputSpeech);
	}
	
	private SpeechletResponse getNewAskResponse(String speechText, String repromptText) {
		OutputSpeech outputSpeech, repromptSpeech;
		outputSpeech = new PlainTextOutputSpeech();
		((PlainTextOutputSpeech) outputSpeech).setText(speechText);
		repromptSpeech = new PlainTextOutputSpeech();
		((PlainTextOutputSpeech) repromptSpeech).setText(repromptText);
		Reprompt reprompt = new Reprompt();
		reprompt.setOutputSpeech(repromptSpeech);
		return SpeechletResponse.newAskResponse(outputSpeech, reprompt);
	}
}
