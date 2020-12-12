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
{	private BufferedReader  in;			// ������ ���� �����ϸ� null
	private static ArrayList<String>       columnNames= new ArrayList<>();//�Ϲ������� ���ҿ� �������� ������ �� �־�� �ϰų� ����Ʈ ũ�Ⱑ Ŭ���� ArrayList�� ����ϸ� ����.
	private String tableName;
	private LinkedList<String>       D=new LinkedList<>();//����Ʈ�� ù �κ��̳� �߰��� ���Ҹ� ����/������ �ϵ��� ���ٸ� LinkedList�� ����ϸ� ����.
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
	    	System.out.println("xml �����Դϴ�.\n");
		String tablen=in.readLine().trim();
		tableName=tablen.substring(1,tablen.length()-1);//���̺���� ��������
		//System.out.println(tableName+"\n");
		String a;
		int t=0;
		while(!(a=in.readLine().trim()).equals("</"+tableName+">")) {
		if(a.equals("<row>")) {
			t++;
			while(!(a=in.readLine().trim()).equals("</row>")) {
				String data="";
				for(int i=1; i<a.length(); i++) {
					if(a.charAt(i)=='>') {//columnName �Ϸ�Ȱ�
						D.offer(a.substring(i+1));//Queue�� ����
						//System.out.println(a.substring(i+1)+"\n");
						break;
					}
					else {
						data+=a.charAt(i);
					}
				}
				if(t==1) {
				columnNames.add(data);//������ columnName �߰�
				//System.out.println(data+"\n");
				}
			}
		}}
	    }
	    else {System.out.println("�߸��� �����Դϴ�.\n");
	   
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
	//System.out.println("����");
	 if (!D.isEmpty()) {
		 while(!D.isEmpty()) {
			 for(int i=0; i<row.length; i++) {
				 String a=D.poll();//Queue���� �����ϸ� �б�
				 int end=a.indexOf("<");
				 String data=a.substring(0, end);
				 row[i]=data;
				// System.out.println(i+": "+data+"\n");
			 }
			// System.out.println("����\n");
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
	//�߰�

	 public void accept(Visitor v)
	    {
	        v.visit(this);            //��� visit() �޼��带 ȣ������ �������ϴ�.
	    }
public static class Test{
	public static void main( String[] args ) throws IOException
	{	
		FileReader in = new FileReader("C:\\\\\\\\Users\\\\\\\\samsung\\\\\\\\Documents\\\\\\\\GitHub\\\\\\\\designpattern2020\\\\\\\\DP2020Project/people.xml");
		File f = new File("C:\\\\Users\\\\samsung\\\\Documents\\\\GitHub\\\\designpattern2020\\\\DP2020Project","people.xml");
        // ���� ���� ���� �Ǵ�
        if (f.isFile()) {
            System.out.println("������ �ֽ��ϴ�.");
            Table.Importer importer = new XmlImporter(in);
            importer.startTable();
            System.out.println(people.toString());
        }
        else {System.out.println("������ �����ϴ�.");}
        XmlImporter importer = new XmlImporter(in);
        importer.accept(new ViewVisitor()); 
   
    
	}
	}
}

