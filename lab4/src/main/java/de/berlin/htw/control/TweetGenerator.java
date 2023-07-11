package de.berlin.htw.control;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import de.berlin.htw.boundary.dto.Tweet;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * @author Alexander Stanik [stanik@htw-berlin.de]
 */
@ApplicationScoped
public class TweetGenerator {
	
	@ConfigProperty(name = "htw.tweet.subject-filename")
	String subjectFilename;

	@ConfigProperty(name = "quarkus.profile")
	String profile;

	private List<String> subjects;
	
	private List<String> predicates;
	
	private List<String> countries;
	
    public Tweet generateTweet() {
    	return generateTweet(getRandomElement(subjects));
    }

    public Tweet generateTweet(final String subject) {
		Tweet tweet = new Tweet();
		tweet.setMessage(subject + " " + getRandomElement(predicates) + " " + getRandomElement(countries));
		tweet.setAuthor(profile);

    	return tweet;
    }
    
    private String getRandomElement(final List<String> elements) {
    	final Random random = new Random();
    	return elements.get(random.nextInt(elements.size()));
    }

	@PostConstruct
	protected void init() {
		subjects = buildSubjects();
		predicates = buildPredicates();
		countries = buildCountries();
	}

	private List<String> buildSubjects() {
		final List<String> subjects = new ArrayList<>();
		final InputStream is = this.getClass().getResourceAsStream(subjectFilename);
		final InputStreamReader streamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
	    try(BufferedReader br = new BufferedReader(streamReader)) {
	        String line = "";
	        while ((line = br.readLine()) != null) {
	        	subjects.add(line);
	        }
	    } catch (IOException e) {
	    	throw new RuntimeException("Unable to read subjects from " + subjectFilename, e);
	    }
	    return subjects;
	}

	private List<String> buildPredicates() {
		return List.of(
				"fliegt nach",
				"geht nach",
				"pilgert nach",
				"wandert nach",
				"studiert in",
				"arbeitet in",
				"wohnt in",
				"liebt das Essen aus",
				"lernt die Kultur von",
				"verbringt den Urlaub in"
				);
	}

	private List<String> buildCountries() {
    	final List<String> countries = new ArrayList<>();
    	final String[] isoCountries = Locale.getISOCountries();
    	for (String country : isoCountries) {
            Locale locale = new Locale("de", country);
            String name = locale.getDisplayCountry();

            if (!"".equals(name)) {
                countries.add(name);
            }
        }
        return countries;
	}

}
