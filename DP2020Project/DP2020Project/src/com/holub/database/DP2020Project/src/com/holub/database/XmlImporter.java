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

import com.holub.tools.ArrayIterator;

import java.io.*;
import java.util.*;


public class XmlImporter implements Table.Importer
{	private BufferedReader  in;			// ������ ���� �����ϸ� null
	private ArrayList<String>       columnNames;//�Ϲ������� ���ҿ� �������� ������ �� �־�� �ϰų� ����Ʈ ũ�Ⱑ Ŭ���� ArrayList�� ����ϸ� ����.
	private LinkedList<String>       D;//����Ʈ�� ù �κ��̳� �߰��� ���Ҹ� ����/������ �ϵ��� ���ٸ� LinkedList�� ����ϸ� ����.
	private String          tableName;

	public XmlImporter( Reader in )
	{	this.in = in instanceof BufferedReader
						? (BufferedReader)in
                        : new BufferedReader(in)
	                    ;
	}
	public void startTable()			throws IOException
	{
		String firstline=in.readLine().trim();
	    String header="<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"?>";
	
	    if(firstline.equals(header)) {
		String tablen=in.readLine().trim();
		tableName=tablen.substring(1,tablen.length()-1);//���̺���� ��������
		
		String a;
		if((a=in.readLine().trim()).equals("<row>")) {
			while(!(a=in.readLine().trim()).equals("</row>")) {
				String data="";
				for(int i=1; i<a.length(); i++) {
					if(a.charAt(i)=='>') {//columnName �Ϸ�Ȱ�
						D.offer(a.substring(i+1));//Queue�� ����
						break;
					}
					else {
						data+=a.charAt(i);
					}
				}
				columnNames.add(data);//������ columnName �߰�
			}
		}
	    }
	    else {System.out.println("�߸��� �����Դϴ�.\n");
	    }
	}
	public String loadTableName()		throws IOException
	{	return tableName;
	}
	public int loadWidth()			    throws IOException
	{	return columnNames.size();
	}
	public Iterator loadColumnNames()	throws IOException
	{	return new ArrayIterator(columnNames.toArray()); 
	}

	public Iterator loadRow()			throws IOException
	{	 String[] row = new String[columnNames.size()];
		
	 if (!D.isEmpty()) {
		 while(!D.isEmpty()) {
			 for(int i=0; i<row.length; i++) {
				 String a=D.poll();//Queue���� �����ϸ� �б�
				 int end=a.indexOf("<");
				 String data=a.substring(0, end);
				 row[i]=data;
				
			 }
		 }
	 }
	 else {
		 String a= in.readLine().trim();
		 if(a.equals("<row>")) {
			 int i=0;
			 while(!(a=in.readLine().trim()).equals("</row>")) {
				 int start=a.indexOf(">")+1;
				 int end=a.indexOf("<");
				 String data=a.substring(start, end);
				 row[i]=data;
				 i+=1;
			 }
		 }
		 else {return null;}
	 }
	 return new ArrayIterator( row );
	}

	public void endTable() throws IOException {}
	
public static class Test{
	public static void main( String[] args ) throws IOException
	{	
		Table people = TableFactory.create( "people",
					   new String[]{ "First", "Last"		} );
		people.insert( new String[]{ "Allen",	"Holub" 	} );
		people.insert( new String[]{ "Ichabod",	"Crane" 	} );
		people.insert( new String[]{ "Rip",		"VanWinkle" } );
		people.insert( new String[]{ "Goldie",	"Locks" 	} );

		
		 Writer writer = new FileWriter("C://dp2020");
		 people.export(new XmlExporter(writer));
            writer.close();

	}
	}
}
