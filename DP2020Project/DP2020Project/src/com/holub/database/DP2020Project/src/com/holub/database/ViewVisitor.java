
package com.holub.database;

import com.holub.tools.ArrayIterator;

//import snippet.Visitor;

import java.io.*;
import java.util.*;


public class ViewVisitor implements Visitor
{	
	private static final String STATUS = "������� ���� ��ġ : ";
	 @Override
	    public String visit(final XmlImporter name) {
		 System.out.println(STATUS+name);
		 return STATUS ;
	    }
}
