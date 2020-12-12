
package com.holub.database;

import com.holub.tools.ArrayIterator;

//import snippet.Visitor;

import java.io.*;
import java.util.*;


public interface Visitor 
{	
	String visit(XmlImporter name);
}
