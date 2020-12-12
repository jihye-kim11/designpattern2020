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

//import snippet.Visitor;

import java.io.*;
import java.util.*;


public class XmlImporter implements Table.Importer
{	private BufferedReader  in;			// 파일의 끝에 도달하면 null
	private static ArrayList<String>       columnNames= new ArrayList<>();//일반적으로 원소에 랜덤으로 접근할 수 있어야 하거나 리스트 크기가 클수록 ArrayList를 사용하면 좋다.
	private String tableName;
	private LinkedList<String>       D=new LinkedList<>();//리스트의 첫 부분이나 중간에 원소를 삽입/삭제할 일들이 많다면 LinkedList를 사용하면 좋다.
	public static Table people;
	public XmlImporter( Reader in )
	{	this.in = in instanceof BufferedReader
						? (BufferedReader)in
                        : new BufferedReader(in)
	                    ;
	}
	public void startTable()			throws IOException
	{
		String firstline=in.readLine().trim();
	    String header="<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	
	    if(firstline.equals(header)) {
	    	System.out.println("xml 파일입니다.\n");
		String tablen=in.readLine().trim();
		tableName=tablen.substring(1,tablen.length()-1);//테이블네임 가져오기
		//System.out.println(tableName+"\n");
		String a;
		int t=0;
		while(!(a=in.readLine().trim()).equals("</"+tableName+">")) {
		if(a.equals("<row>")) {
			t++;
			while(!(a=in.readLine().trim()).equals("</row>")) {
				String data="";
				for(int i=1; i<a.length(); i++) {
					if(a.charAt(i)=='>') {//columnName 완료된것
						D.offer(a.substring(i+1));//Queue에 삽입
						//System.out.println(a.substring(i+1)+"\n");
						break;
					}
					else {
						data+=a.charAt(i);
					}
				}
				if(t==1) {
				columnNames.add(data);//데이터 columnName 추가
				//System.out.println(data+"\n");
				}
			}
		}}
	    }
	    else {System.out.println("잘못된 파일입니다.\n");
	   
	    }
	    String[] Name = new String[columnNames.size()];
	    Name = columnNames.toArray(Name);
	    people = TableFactory.create(tableName, Name);
       
	    loadRow();
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
	//System.out.println("접근");
	 if (!D.isEmpty()) {
		 while(!D.isEmpty()) {
			 for(int i=0; i<row.length; i++) {
				 String a=D.poll();//Queue에서 제거하며 읽기
				 int end=a.indexOf("<");
				 String data=a.substring(0, end);
				 row[i]=data;
				// System.out.println(i+": "+data+"\n");
			 }
			// System.out.println("시점\n");
			 people.insert(row);
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
	//추가

	 public void accept(Visitor v)
	    {
	        v.visit(this);            //어느 visit() 메서드를 호출할지 결정납니다.
	    }
public static class Test{
	public static void main( String[] args ) throws IOException
	{	
		FileReader in = new FileReader("C:\\\\\\\\Users\\\\\\\\samsung\\\\\\\\Documents\\\\\\\\GitHub\\\\\\\\designpattern2020\\\\\\\\DP2020Project/people.xml");
		File f = new File("C:\\\\Users\\\\samsung\\\\Documents\\\\GitHub\\\\designpattern2020\\\\DP2020Project","people.xml");
        // 파일 존재 여부 판단
        if (f.isFile()) {
            System.out.println("파일이 있습니다.");
            Table.Importer importer = new XmlImporter(in);
            importer.startTable();
            System.out.println(people.toString());
        }
        else {System.out.println("파일이 없습니다.");}
        XmlImporter importer = new XmlImporter(in);
        importer.accept(new ViewVisitor()); 
   
    
	}
	}
}

