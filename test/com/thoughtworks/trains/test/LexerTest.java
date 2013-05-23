package com.thoughtworks.trains.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import com.thoughtworks.trains.Lexer;
import com.thoughtworks.trains.Lexer.Token;
import com.thoughtworks.trains.Lexer.TokenType;

public class LexerTest {

	//@Test
	public void testLex_Arithmetic() {
    String input = "11 + 22 - 33";

    // Build tokens
    List<Token> expectedTokens = new LinkedList<Token>();
    expectedTokens.add(new Token(TokenType.NUMBER, "11"));
    expectedTokens.add(new Token(TokenType.BINARYOP, "+"));
    expectedTokens.add(new Token(TokenType.NUMBER, "22"));
    expectedTokens.add(new Token(TokenType.BINARYOP, "-"));
    expectedTokens.add(new Token(TokenType.NUMBER, "33"));
    
    ArrayList<Token> tokens = Lexer.lex(input);
    
    // Number of Tokens
    assertEquals("Number of tokens",  expectedTokens.size(), tokens.size());
    // Tokens are the same
    Iterator<Token> expectedTokensIt = expectedTokens.iterator();
    Iterator<Token> tokensIt = tokens.iterator();
    while (expectedTokensIt.hasNext()) {
    	assertEquals("Token", expectedTokensIt.next(), tokensIt.next());
    }
	}
	
	//@Test
	public void testLex_Graph() {
		String input = "Graph: AB5, BC4, CD8, DC8, DE6, AD5, CE2, EB3, AE7";
		
		//TODO Build tokens
    List<Token> expectedTokens = new LinkedList<Token>();

    ArrayList<Token> tokens = Lexer.lex(input);
    
    // Number of Tokens
    assertEquals("Number of tokens",  expectedTokens.size(), tokens.size());
    // Tokens are the same
    Iterator<Token> expectedTokensIt = expectedTokens.iterator();
    Iterator<Token> tokensIt = tokens.iterator();
    while (expectedTokensIt.hasNext()) {
    	assertEquals("Token", expectedTokensIt.next(), tokensIt.next());
    }
	}

}
