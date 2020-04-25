package org.xewn.xmlbeans;

import org.apache.xmlbeans.XmlObject;
import org.xewn.xmlbeans.LemmaDocument.Lemma;
import org.xewn.xmlbeans.SenseDocument.Sense;
import org.xewn.xmlbeans.SynsetDocument.Synset;
import org.xewn.xmlbeans.SyntacticBehaviourDocument.SyntacticBehaviour;

public class Query
{
	private static final String DECLARE_XQ_NAMESPACE = "declare namespace xq='##local';";

	private static final String DECLARE_DC_NAMESPACE = "declare namespace dc='http://purl.org/dc/elements/1.1/';";

	private static final String DECLARE_NAMESPACES = DECLARE_XQ_NAMESPACE + DECLARE_DC_NAMESPACE;

	// private static String QUERY_SYNSET = DECLARE_NAMESPACES + "$this/LexicalResource/Lexicon/Synset";
	private static final String QUERY_SYNSET = DECLARE_NAMESPACES + "$this//Synset";

	//private static String QUERY_SENSE = DECLARE_NAMESPACES + "$this/LexicalResource/Lexicon/LexicalEntry/Sense";
	private static final String QUERY_SENSE = DECLARE_NAMESPACES + "$this//Sense";

	//private static String QUERY_SYNTACTIC_BEHAVIOUR = DECLARE_NAMESPACES + "$this/LexicalResource/Lexicon/SyntacticBehaviour";
	private static final String QUERY_SYNTACTIC_BEHAVIOUR = DECLARE_NAMESPACES + "$this//SyntacticBehaviour";

	private static final String QUERY_LEMMA_FROM_SENSE = DECLARE_NAMESPACES + "$this/../Lemma";

	private static final String QUERY_LEMMAS_FROM_SYNSET = DECLARE_NAMESPACES + "$this/../LexicalEntry/Lemma";

	private static final String QUERY_SYNSET_FROM_SENSE = DECLARE_NAMESPACES + "$this/../../Synset";

	public static Synset querySynsetById(XmlObject top, String id)
	{
		// Query from the top.
		String query = QUERY_SYNSET + "[@id='" + id + "']";
		XmlObject[] result = top.selectPath(query);
		if (result.length > 1)
			throw new IllegalArgumentException();
		if (result.length == 0)
			throw new IllegalArgumentException();
		return (Synset) result[0];
	}

	public static Sense querySenseById(XmlObject top, String id)
	{
		String query = QUERY_SENSE + "[@id='" + id + "']";
		XmlObject[] result = top.selectPath(query);
		if (result.length > 1)
			throw new IllegalArgumentException();
		if (result.length == 0)
			throw new IllegalArgumentException();
		return (Sense) result[0];
	}

	public static Sense querySenseBySensekey(XmlObject top, String sk)
	{
		String query = QUERY_SENSE + "[@sensekey='" + sk + "']";
		XmlObject[] result = top.selectPath(query);
		if (result.length > 1)
			throw new IllegalArgumentException();
		if (result.length == 0)
			throw new IllegalArgumentException();
		return (Sense) result[0];
	}

	public static Sense querySenseByDcIdentifier(XmlObject top, String sk)
	{
		String query = QUERY_SENSE + "[@dc:identifier='" + sk + "']";
		XmlObject[] result = top.selectPath(query);
		if (result.length > 1)
			throw new IllegalArgumentException();
		if (result.length == 0)
			throw new IllegalArgumentException();
		return (Sense) result[0];
	}

	public static Lemma queryLemmaFromSense(Sense sense)
	{
		XmlObject[] result = sense.selectPath(QUERY_LEMMA_FROM_SENSE);
		if (result.length > 1)
			throw new IllegalArgumentException();
		if (result.length == 0)
			throw new IllegalArgumentException();
		return (Lemma) result[0];
	}

	public static Lemma[] queryLemmasFromSynset(Synset synset)
	{
		String synsetId = synset.getId();
		String query = QUERY_LEMMAS_FROM_SYNSET + "[../Sense/@synset='" + synsetId + "']";
		XmlObject[] result = synset.selectPath(query);
		if (result.length == 0)
			throw new IllegalArgumentException();
		return (Lemma[]) result;
	}

	public static Synset querySynsetFromSense(Sense sense)
	{
		String synsetId = sense.getSynset();
		String query = QUERY_SYNSET_FROM_SENSE + "[@id='" + synsetId + "']";
		XmlObject[] result = sense.selectPath(query);
		if (result.length > 1)
			throw new IllegalArgumentException();
		if (result.length == 0)
			throw new IllegalArgumentException();
		return (Synset) result[0];
	}

	public static Sense[] querySensesOf(XmlObject top, String word)
	{
		String query = QUERY_SENSE + "[../Lemma/@writtenForm='" + word + "']";

		// Query from the top.
		XmlObject[] result = top.selectPath(query);
		return (Sense[]) result;
	}

	public static Synset[] querySynsets(XmlObject top, String query)
	{
		// Query from the top.
		XmlObject[] result = top.selectPath(query);
		return (Synset[]) result;
	}

	public static Sense[] querySenses(XmlObject top, String query)
	{
		// Query from the top.
		XmlObject[] result = top.selectPath(query);
		return (Sense[]) result;
	}

	public static SyntacticBehaviour querySyntacticBehaviourById(XmlObject top, String id)
	{
		String query = QUERY_SYNTACTIC_BEHAVIOUR + "[@id='" + id + "']";
		XmlObject[] result = top.selectPath(query);
		if (result.length > 1)
			throw new IllegalArgumentException();
		if (result.length == 0)
			throw new IllegalArgumentException();
		return (SyntacticBehaviour) result[0];
	}

	public static String queryVerbFrameById(XmlObject top, String id)
	{
		SyntacticBehaviour sb = querySyntacticBehaviourById(top, id);
		return sb.getSubcategorizationFrame();
	}

	public static String queryVerbTemplateById(XmlObject top, String id)
	{
		SyntacticBehaviour sb = querySyntacticBehaviourById(top, id);
		return sb.getSentenceTemplate();
	}
}
