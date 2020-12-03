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

public class XmlExporter implements Table.Exporter
{	private final Writer out;
	private 	  int	 width;
    private       int    height;
    private String[]     columnNames;
    private String       tableName;
	public XmlExporter( Writer out )
	{	this.out = out;
	}
	
	@Override
	public void startTable() throws IOException{
		out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
	     
	}

	public void storeMetadata( String tableName,
							   int width,
							   int height,
							   Iterator columnNames ) throws IOException

	{	this.width = width;
	 this.height = height;
     this.tableName = tableName;

     out.write(tableName == null ? "<anonymous>" : "<" + tableName + ">");
     out.write("\n");

     this.columnNames = new String[width];
     for (int i = 0; columnNames.hasNext();) {
         this.columnNames[i++] = columnNames.next().toString();
     }
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

			javax.swing.JFrame frame = new javax.swing.JFrame();
			frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

			JTableExporter tableBuilder = new JTableExporter();
			people.export( tableBuilder );

			frame.getContentPane().add(
					new JScrollPane( tableBuilder.getJTable() ) );
			frame.pack();
			frame.setVisible( true );
		}
	}
}
