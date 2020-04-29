import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlException;
import org.junit.Before;
import org.junit.Test;
import org.xewn.xmlbeans.*;
import org.xewn.xmlbeans.DefinitionDocument.Definition;
import org.xewn.xmlbeans.ExampleDocument.Example;
import org.xewn.xmlbeans.LemmaDocument.Lemma;
import org.xewn.xmlbeans.SenseDocument.Sense;
import org.xewn.xmlbeans.SenseRelationDocument.SenseRelation;
import org.xewn.xmlbeans.SynsetDocument.Synset;
import org.xewn.xmlbeans.SynsetRelationDocument.SynsetRelation;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class QueryTest
{
	private static final String WORD = "spread";
	private static final String WORD2 = "suffer";
	private static final String SYNSET_ID = "ewn-00054345-v";
	private static final String SENSE_ID = "ewn-generate-v-00054345-07";
	private static final String SK = "generate%2:29:00::";
	private static final String DC_IDENTIFIER = "generate%2:29:00::";
	private static final String VERB_FRAME = "ewn-sb-28";
	private static final String VERB_TEMPLATE = "ewn-st-28";

	private LexicalResourceDocument document;

	@Before public void getDocument() throws IOException, XmlException
	{
		String xewnHome = System.getenv("XEWNHOME") + File.separator + "xsrc";
		final File xmlFile = new File(xewnHome, "wn-verb.communication.xml");
		this.document = LexicalResourceDocument.Factory.parse(xmlFile);
	}

	@Test public void queryWord()
	{
		assertNotNull(this.document);
		Sense[] senses = Query.querySensesOf(this.document, WORD);
		assertNotNull(senses);

		for (Sense sense : senses)
		{
			walkSense(sense);

			Synset synset = Query.querySynsetFromSense(sense);
			assertNotNull(synset);
			walkSynset(synset);
			System.out.println();
		}
	}

	public void walkSense(Sense sense)
	{
		System.out.printf("sense id: %s%n", sense.getId());
		System.out.printf("\tn: %s%n", sense.getN());
		System.out.printf("\torder: %s%n", sense.getOrder());
		System.out.printf("\tidx: %s%n", sense.getSenseidx());
		System.out.printf("\tlexid: %s%n", sense.getLexid());
		System.out.printf("\tlexfile: %s%n", sense.getLexfile());
		System.out.printf("\tkey: %s%n", sense.getSensekey());
		System.out.printf("\tdc:identifier: %s%n", sense.getIdentifier());

		Lemma lemma = Query.queryLemmaFromSense(sense);
		assertNotNull(lemma);
		System.out.printf("\tlemma: %s%n", lemma.getWrittenForm());

		// adj
		AdjPositionType.Enum adjposition = sense.getAdjposition();
		AdjPositionType.Enum adjPosition = sense.getAdjPosition();
		String adjHead = sense.getAdjHead();
		boolean isAdjHead = sense.getAdjIsHead();
		System.out.printf("\tadjHead: %s isHead: %b adjposition: %s adjPosition: %s%n", adjHead, isAdjHead, adjposition, adjPosition);

		// verb
		List<String> verbFrameIds = (List<String>) sense.getVerbFrames();
		if (verbFrameIds != null)
			for (String verbFrameId : verbFrameIds)
			{
				assertNotNull(verbFrameId);
				String verbFrame = Query.queryVerbFrameById(this.document, verbFrameId);
				assertNotNull(verbFrame);
				System.out.printf("\tverb frame %s %s%n", verbFrameId, verbFrame);
			}
		List<String> verbTemplates = (List<String>) sense.getVerbTemplates();
		if (verbTemplates != null)
			for (String verbTemplateId : verbTemplates)
			{
				assertNotNull(verbTemplateId);
				String verbTemplate = Query.queryVerbTemplateById(this.document, verbTemplateId);
				assertNotNull(verbTemplate);
				System.out.printf("\tverb template %s %s%n", verbTemplateId, verbTemplate);
			}
		for (SenseRelation senseRelation : sense.getSenseRelationArray())
		{
			String target = senseRelation.getTarget();
			assertNotNull(target);
			SenseRelationType.Enum type = senseRelation.getRelType();
			assertNotNull(type);
			System.out.printf("\tsense relation %s %s%n", type, target);
		}
	}

	public void walkSynset(Synset synset)
	{
		assertNotNull(this.document);
		assertNotNull(synset);

		System.out.printf("synset id: %s%n", synset.getId());
		Lemma[] lemmas = Query.queryLemmasFromSynset(synset);
		assertTrue(lemmas.length > 0);
		for (Lemma lemma : lemmas)
		{
			System.out.printf("\tmember: %s%n", lemma.getWrittenForm());
		}
		Definition definition = synset.getDefinitionArray(0);
		SimpleValue definitionValue = ((SimpleValue) definition);
		System.out.printf("\tdefinition '%s'%n", definitionValue.getStringValue());
		// Alternatively
		// System.out.printf("\tdefinition %s%n", definition.getStringValue());

		for (Example example : synset.getExampleArray())
		{
			SimpleValue exampleValue = ((SimpleValue) example);
			System.out.printf("\texample %s%n", exampleValue.getStringValue());
			// Alternatively
			// System.out.printf("\texample %s%n", example.getStringValue());
		}

		for (SynsetRelation synsetRelation : synset.getSynsetRelationArray())
		{
			String target = synsetRelation.getTarget();
			assertNotNull(target);
			SynsetRelationType.Enum type = synsetRelation.getRelType();
			assertNotNull(type);
			System.out.printf("\tsense relation %s %s%n", type, target);
		}
	}
}
