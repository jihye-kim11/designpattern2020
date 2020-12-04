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
{	private BufferedReader  in;			// 파일의 끝에 도달하면 null
	private ArrayList<String>       columnNames;//일반적으로 원소에 랜덤으로 접근할 수 있어야 하거나 리스트 크기가 클수록 ArrayList를 사용하면 좋다.
	private LinkedList<String>       D;//리스트의 첫 부분이나 중간에 원소를 삽입/삭제할 일들이 많다면 LinkedList를 사용하면 좋다.
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
		tableName=tablen.substring(1,tablen.length()-1);//테이블네임 가져오기
		
		String a;
		if((a=in.readLine().trim()).equals("<row>")) {
			while(!(a=in.readLine().trim()).equals("</row>")) {
				String data="";
				for(int i=1; i<a.length(); i++) {
					if(a.charAt(i)=='>') {//columnName 완료된것
						D.offer(a.substring(i+1));//Queue에 삽입
						break;
					}
					else {
						data+=a.charAt(i);
					}
				}
				columnNames.add(data);//데이터 columnName 추가
			}
		}
	    }
	    else {System.out.println("잘못된 파일입니다.\n");
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
				 String a=D.poll();//Queue에서 제거하며 읽기
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
