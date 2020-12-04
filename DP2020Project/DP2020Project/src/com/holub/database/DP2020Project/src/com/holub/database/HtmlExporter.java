/*  (c) 2004 Allen I. Holub. All rights reserved.
 *
 *  This code may be used freely by yourself with the following
 *  restrictions:
 *
 *  o Your splash screen, about box, or equivalent, must include
 *    Allen Holub's name, copyright, and URL. For example:
 *
 *      This program contains Allen Holub's SQL package.<br>
 *      (c) 2005 Allen I. Holub. All Rights Reserved.<br>
 *              http://www.holub.com<br>
 *
 *    If your program does not run interactively, then the foregoing
 *    notice must appear in your documentation.
 *
 *  o You may not redistribute (or mirror) the source code.
 *
 *  o You must report any bugs that you find to me. Use the form at
 *    http://www.holub.com/company/contact.html or send email to
 *    allen@Holub.com.
 *
 *  o The software is supplied <em>as is</em>. Neither Allen Holub nor
 *    Holub Associates are responsible for any bugs (or any problems
 *    caused by bugs, including lost productivity or data)
 *    in any of this code.
 */
package com.holub.database;

import com.holub.database.Table;

import java.io.*;
import java.util.*;
import java.io.Writer;
import java.io.IOException;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

public class HtmlExporter implements Table.Exporter
{	private final Writer out;
	private 	  int	 width;

	public HtmlExporter( Writer out )
	{	this.out = out;
	}
	
	@Override
	public void startTable() throws IOException{
	     out.write("<!DOCTYPE html>\n");
	     out.write("<html>\n");
	     out.write("<head>\n");
	     out.write("<title>HtmlExporter</title>\n");
	     out.write("<style>\n");
	     out.write("table { width: 100%; }\n");
	     out.write("th, td { border: 1px solid #dadada; }\n");
	     out.write("</style>\n");
	     out.write("</head>\n");
	     out.write("<body>\n");
	     out.write("<table>\n");
	     
	}

	public void storeMetadata( String tableName,
							   int width,
							   int height,
							   Iterator columnNames ) throws IOException

	{	this.width = width;
	    out.write("<caption>");
		out.write(tableName == null ? "<anonymous>" : tableName );
		out.write("<caption>\n");
		out.write("<tr>\n");
		 while( columnNames.hasNext() ) {
	            Object datum = columnNames.next();
	            out.write("<th>");
	            if( datum != null ) {
	                out.write(datum.toString());
	            }
	            out.write("</th>\n");

	            width =width- 1;
	        }
		out.write("</tr>\n");
	}

	public void storeRow( Iterator data ) throws IOException
	{	int i = width;
	 out.write("<tr>\n");
		while( data.hasNext() )
		{	Object datum = data.next();
		 out.write("<td>");
			if( datum != null )	{
				out.write( datum.toString() );}

			out.write("</td>\n");
			
			i=i-1;
		}
		out.write("</tr>\n");
	}

	public void endTable() throws IOException {
		 out.write("</table>\n");
        out.write("</body>\n");
	}
	
	public static class Test
	{ 	public static void main( String[] args ) throws IOException
		{	
			Table people = TableFactory.create( "people",
						   new String[]{ "First", "Last"		} );
			people.insert( new String[]{ "Allen",	"Holub" 	} );
			people.insert( new String[]{ "Ichabod",	"Crane" 	} );
			people.insert( new String[]{ "Rip",		"VanWinkle" } );
			people.insert( new String[]{ "Goldie",	"Locks" 	} );

			// Writer writer = new FileWriter("C:\\dp2020/people.html");	
			 Writer writer = new FileWriter("C:\\Users\\samsung\\Documents\\GitHub\\designpattern2020\\DP2020Project/people.html");
			 people.export(new HtmlExporter(writer));
	            writer.close();

		}
	}
}
