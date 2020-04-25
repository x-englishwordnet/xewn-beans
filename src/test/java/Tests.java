import org.apache.xmlbeans.XmlException;
import org.junit.Before;
import org.junit.Test;
import org.xewn.xmlbeans.*;
import org.xewn.xmlbeans.DefinitionDocument.Definition;
import org.xewn.xmlbeans.ExampleDocument.Example;
import org.xewn.xmlbeans.LemmaDocument.Lemma;
import org.xewn.xmlbeans.LexicalEntryDocument.LexicalEntry;
import org.xewn.xmlbeans.LexicalResourceDocument.LexicalResource;
import org.xewn.xmlbeans.LexiconDocument.Lexicon;
import org.xewn.xmlbeans.SenseDocument.Sense;
import org.xewn.xmlbeans.SenseRelationDocument.SenseRelation;
import org.xewn.xmlbeans.SynsetDocument.Synset;
import org.xewn.xmlbeans.SynsetRelationDocument.SynsetRelation;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import static org.junit.Assert.*;

public class Tests
{
	private static final String WORD = "generate";
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
		String xewnHome = System.getenv("XEWNHOME") + File.separator + "BUILD" + File.separator + "xsrc";
		final File xmlFile = new File(xewnHome, "wn-verb.body.xml");
		this.document = LexicalResourceDocument.Factory.parse(xmlFile);
	}

	@Test public void getSynsetById()
	{
		assertNotNull(this.document);
		Synset synset = Query.querySynsetById(this.document, SYNSET_ID);
		assertNotNull(synset);
		assertEquals(SYNSET_ID, synset.getId());
		Definition definition = synset.getDefinitionArray(0);
		System.out.printf("%s: %s '%s'%n", "getSynsetById", synset.getId(), definition);
	}

	@Test public void getLemmasFromSynset()
	{
		assertNotNull(this.document);
		Synset synset = Query.querySynsetById(this.document, SYNSET_ID);
		assertNotNull(synset);
		assertEquals(SYNSET_ID, synset.getId());
		Lemma[] lemmas = Query.queryLemmasFromSynset(synset);
		assertTrue(lemmas.length > 0);
		for (Lemma lemma : lemmas)
		{
			System.out.printf("%s: %s%n", "getLemmasFromSynset", lemma.getWrittenForm());
		}
	}

	@Test public void getSenseById()
	{
		assertNotNull(this.document);
		Sense sense = Query.querySenseById(this.document, SENSE_ID);
		assertNotNull(sense);
		assertEquals(SENSE_ID, sense.getId());
		String sensekey = sense.getSensekey();
		assertNotNull(sensekey);
		System.out.printf("%s: %s%n", "getSenseById", sensekey);
	}

	@Test public void getLemmaFromSense()
	{
		assertNotNull(this.document);
		Sense sense = Query.querySenseById(this.document, SENSE_ID);
		assertNotNull(sense);
		assertEquals(SENSE_ID, sense.getId());
		Lemma lemma = Query.queryLemmaFromSense(sense);
		assertNotNull(lemma);
		System.out.printf("%s: %s%n", "getLemmaFromSense", lemma.getWrittenForm());
	}

	@Test public void getSynsetFromSense()
	{
		assertNotNull(this.document);
		Sense sense = Query.querySenseById(this.document, SENSE_ID);
		assertNotNull(sense);
		assertEquals(SENSE_ID, sense.getId());
		Synset synset = Query.querySynsetFromSense(sense);
		assertNotNull(synset);
		System.out.printf("%s: %s%n", "getSynsetFromSense", synset.getDefinitionArray(0));
	}

	@Test public void getSenseBySensekey()
	{
		assertNotNull(this.document);
		Sense sense = Query.querySenseBySensekey(this.document, SK);
		assertNotNull(sense);
		assertEquals(SK, sense.getSensekey());
		String sensekey = sense.getSensekey();
		System.out.printf("%s: %s%n", "getSenseBySensekey", sensekey);
	}

	@Test public void getSenseByDcIdentifier()
	{
		assertNotNull(this.document);
		Sense sense = Query.querySenseByDcIdentifier(this.document, DC_IDENTIFIER);
		assertNotNull(sense);
		assertEquals(DC_IDENTIFIER, sense.getSensekey());
		String sensekey = sense.getSensekey();
		System.out.printf("%s: %s%n", "getSenseByDcIdentifier", sensekey);
	}

	@Test public void getSensesByWord()
	{
		assertNotNull(this.document);
		Sense[] senses = Query.querySensesOf(this.document, WORD2);
		assertNotNull(senses);
		for (Sense sense : senses)
		{
			System.out.printf("%s: %s%n", "getSensesOf", sense.getSensekey());
		}
	}

	@Test public void getVerbFrameById()
	{
		assertNotNull(this.document);
		String verbFrame = Query.queryVerbFrameById(this.document, VERB_FRAME);
		assertNotNull(verbFrame);
		System.out.printf("%s: %s%n", "getVerbFrameById", verbFrame);
	}

	@Test public void getVerbTemplateById()
	{
		assertNotNull(this.document);
		String verbTemplate = Query.queryVerbTemplateById(this.document, VERB_TEMPLATE);
		assertNotNull(verbTemplate);
		System.out.printf("%s: %s%n", "getVerbTemplateById", verbTemplate);
	}

	@Test public void scanSenses() throws IOException, IOException, XmlException
	{
		assertNotNull(this.document);
		final LexicalResource lexicalResource = document.getLexicalResource();
		assertNotNull(lexicalResource);
		final Lexicon lexicon = lexicalResource.getLexiconArray()[0];
		assertNotNull(lexicon);

		for (LexicalEntry entry : lexicon.getLexicalEntryArray())
		{
			Lemma lemma = entry.getLemma();
			assertNotNull(lemma);
			PartOfSpeechType.Enum pos = lemma.getPartOfSpeech();
			assertNotNull(pos);
			String writtenForm = lemma.getWrittenForm();
			assertNotNull(writtenForm);
			WrittenFormType writtenFormType = lemma.xgetWrittenForm();
			assertNotNull(writtenFormType);

			for (Sense sense : entry.getSenseArray())
			{
				String id = sense.getId();
				assertNotNull(id);
				String synsetId = sense.getSynset();
				assertNotNull(synsetId);

				BigInteger n = sense.getN();
				assertNotNull(n);
				BigInteger idx = sense.getSenseidx();
				assertNotNull(idx);
				LexFileType.Enum lexFile = sense.getLexfile();
				//assertNotNull(lexFile);
				String lexId = sense.getLexid();
				assertNotNull(lexId);
				BigInteger order = sense.getOrder();
				assertNotNull(order);
				String sensekey = sense.getSensekey();
				assertNotNull(sensekey);
				String identifier = sense.getIdentifier();
				assertNotNull(identifier);

				// adj
				AdjPositionType.Enum adjPosition = sense.getAdjPosition();
				String adjHead = sense.getAdjHead();
				boolean isAdjHead = sense.getAdjIsHead();

				// verb
				List<String> verbFrames = (List<String>) sense.getVerbFrames();
				if (verbFrames != null)
					for (String verbFrame : verbFrames)
					{
						assertNotNull(verbFrame);
						//System.out.printf("verb frame %s%n", verbFrame);
					}
				List<String> verbTemplates = (List<String>) sense.getVerbTemplates();
				if (verbTemplates != null)
					for (String verbTemplate : verbTemplates)
					{
						assertNotNull(verbTemplate);
						//System.out.printf("verb template %s%n", verbTemplate);
					}
				for (SenseRelation senseRelation : sense.getSenseRelationArray())
				{
					String target = senseRelation.getTarget();
					assertNotNull(target);
					SenseRelationType type = senseRelation.xgetRelType();
					assertNotNull(type);
					SenseIDREFType idref = senseRelation.xgetTarget();
					assertNotNull(idref);
				}
			}
		}
	}

	@Test public void scanSynsets() throws IOException, IOException, XmlException
	{
		assertNotNull(this.document);
		final LexicalResource lexicalResource = this.document.getLexicalResource();
		assertNotNull(lexicalResource);
		final Lexicon lexicon = lexicalResource.getLexiconArray()[0];
		assertNotNull(lexicon);

		for (Synset synset : lexicon.getSynsetArray())
		{
			PartOfSpeechType.Enum pos = synset.getPartOfSpeech();
			assertNotNull(pos);
			DefinitionDocument.Definition definition = synset.getDefinitionArray(0);
			assertNotNull(definition);

			for (Example example : synset.getExampleArray())
			{
				assertNotNull(example);
			}
			for (SynsetRelation synsetRelation : synset.getSynsetRelationArray())
			{
				String target = synsetRelation.getTarget();
				assertNotNull(target);
				SynsetRelationType type = synsetRelation.xgetRelType();
				assertNotNull(type);
				SynsetIDREFType idref = synsetRelation.xgetTarget();
				assertNotNull(idref);
			}
		}
	}
}
