/*
Libraries:
JRE System Library[JavaSE-1.7]
postgresql-9.2-1003.jdbc4.jar sample

for Windows: javac sample.java
             java sample

for Linux:
javac sample.java
java -cp .:postgresql-9.2-1003.jdbc4.jar sample

if not compile,please reset the following:
String usr;
String pwd;
String url;

*/

/*
 subjuct=DBMS
 name : Ruian Zeng
 In order to achieve "minimal scans":
 I used two different 2-dimensional lists for querying qualified results for question 1,2,3. The "vertical" one is to store all the results("horizontal" list) 
 and the "horizontal" one is to store every result's data(every instance of class Question1),I use class Question1 to store data of every horizontal list, this is my main structure I used.
 I also create a vector to store the first two columns' data in order to judge whether this row in the DB already exist in the list, if exist just add them according to 
 the proper state and time, changing the corresponding data in the instance of the class Queation1, if not, add a new "horizontal" list to the "vertical" list, and then scan next 
 row in DB until end
 */
import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.text.Format;



public class sample {

	public static void main(String[] args) {
		String usr ="postgres";
		String pwd ="XXXXXX";
		String url ="jdbc:postgresql://localhost:5432/Test";


		try {
			Class.forName("org.postgresql.Driver");
			System.out.println("Success loading Driver!");
		}

		catch (Exception e) {
			System.out.println("Fail loading Driver!");
			e.printStackTrace();
		}

		try {
			Connection conn = DriverManager.getConnection(url, usr, pwd);
			System.out.println("Success connecting server!");

			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Sales");

			rs.next();//read the first row 
			//create two different 2-dimensional lists in order to store all qualified data for three question (one for question1, one for question2,3)
			//using class Qustion1 to store all the qualified data for qustion1 and question2,3
			Question1 Qr = new Question1();//pretending this as horizontal list for the question 1 in order to store every instance of class Question1 and then be added to "vertical" list
			Question1 Qr23 = new Question1();//pretending this as horizontal list for the question 2,3 in order to store every instance of class Question1 and then be added to "vertical" list
			Question1 head, next;
			Question1 head23, next23;
			List<Question1> Q1 = new ArrayList<Question1>();// pretending this as vertical list to store all the qualified "rows"(instances of class Question1) for question1
			List<Question1> Q23 = new ArrayList<Question1>();//pretending this as vertical list to store all the qualified "rows"(instances of class Question1) for question2,3
			Q1.add(Qr);
			head = Qr;//combine the pointer head and next for traveling every row of vertical list(horizontal list) for question1
			next = Qr;
			Q23.add(Qr23);//combine the pointer head and next for traveling every row of vertical list(horizontal list) for question2,3
			head23 = Qr23;
			next23 = Qr23;
			Vector<String> judge = new Vector<String>();//store the first two columns in order to judge whether the first two columns already exist in the list
			Vector<String> judge23 = new Vector<String>();
       //initialize the two 2-dimensional lists
			if (rs.getString(6).equals("NY")) {

				head.sumNY = rs.getInt(7);
				head.sumNJ = 0;
				head.sumCT = 0;
				head.sumPA = 0;
				head.countNY = 1;
				head.countNJ = 0;
				head.countCT = 0;
				head.countPA = 0;

				head23.maxDay = rs.getInt(3);
				head23.maxMonth = rs.getInt(4);
				head23.maxYear = rs.getInt(5);
				head23.minDay = rs.getInt(3);
				head23.minMonth = rs.getInt(4);
				head23.minYear = rs.getInt(5);
				head23.maxState = "NY";
				head23.minState = "NY";
				head23.Max_Quant = rs.getInt(7);
				head23.Min_Quant = rs.getInt(7);
			}
			if (rs.getString(6).equals("NJ")) {

				head23.maxDay = rs.getInt(3);
				head23.maxMonth = rs.getInt(4);
				head23.maxYear = rs.getInt(5);
				head23.minDay = rs.getInt(3);
				head23.minMonth = rs.getInt(4);
				head23.minYear = rs.getInt(5);
				head23.maxState = "NJ";
				head23.minState = "NJ";
				head23.Max_Quant = rs.getInt(7);
				head23.Min_Quant = rs.getInt(7);
			}
			if (rs.getString(6).equals("PA")) {
				head23.maxDay = rs.getInt(3);
				head23.maxMonth = rs.getInt(4);
				head23.maxYear = rs.getInt(5);
				head23.minDay = rs.getInt(3);
				head23.minMonth = rs.getInt(4);
				head23.minYear = rs.getInt(5);
				head23.maxState = "PA";
				head23.minState = "PA";
				head23.Max_Quant = rs.getInt(7);
				head23.Min_Quant = rs.getInt(7);
			}
			if (rs.getString(6).equals("CT")) {

				head23.maxDay = rs.getInt(3);
				head23.maxMonth = rs.getInt(4);
				head23.maxYear = rs.getInt(5);
				head23.minDay = rs.getInt(3);
				head23.minMonth = rs.getInt(4);
				head23.minYear = rs.getInt(5);
				head23.maxState = "CT";
				head23.minState = "CT";
				head23.Max_Quant = rs.getInt(7);
				head23.Min_Quant = rs.getInt(7);
			}

			if (rs.getString(6).equals("NJ")
					&& (rs.getInt(5) >= 1990 && rs.getInt(5) <= 1995)) {

				head.sumNY = 0;
				head.sumNJ = rs.getInt(7);
				head.sumCT = 0;
				head.sumPA = 0;
				head.countNY = 0;
				head.countNJ = 1;
				head.countCT = 0;
				head.countPA = 0;

			}

			if (rs.getString(6).equals("CT")
					&& (rs.getInt(5) >= 1990 && rs.getInt(5) <= 1995)) {

				head.sumNY = 0;
				head.sumNJ = 0;
				head.sumCT = rs.getInt(7);
				head.sumPA = 0;
				head.countNY = 0;
				head.countNJ = 0;
				head.countCT = 1;
				head.countPA = 0;

			}
			if ((rs.getString(6).equals("CT") && (rs.getInt(5) < 1990 || rs
					.getInt(5) > 1995))
					|| (rs.getString(6).equals("NJ") && (rs.getInt(5) < 1990 || rs
							.getInt(5) > 1995)) || rs.getString(6).equals("PA")) {

				head.sumNJ = 0;
				head.sumNY = 0;
				head.sumCT = 0;
				head.sumPA = 0;
				head.countNY = 0;
				head.countNJ = 0;
				head.countCT = 0;
				head.countPA = 0;
			}

			head.Cust = rs.getString(1);
			head.Prod = rs.getString(2);
			head.cust_prod = rs.getString(1) + rs.getString(2);
			head23.allcount = 1;
			head23.allSum = rs.getInt(7);
			head23.Cust = rs.getString(1);
			head23.Prod = rs.getString(2);
			head23.cust_prod = rs.getString(1) + rs.getString(2);

			judge.add(head.cust_prod);
			judge23.add(head23.cust_prod);
			boolean Presave, Presave23;
			int nn = 0, nn23 = 0;
            //next row
			while (rs.next()) {
				Presave = judge.contains(rs.getString(1) + rs.getString(2));//for question1:whether the first two column already exist, return true:exist, false: not exist
				Presave23 = judge23.contains(rs.getString(1) + rs.getString(2));//for question 2,3 

				if (true == Presave) {//for the question1, if true, means current customer and product already exist in the list, move the pointer to the row where current customer and product are
					while (!next.cust_prod.contentEquals(rs.getString(1)
							+ rs.getString(2))) {
						next = Q1.get(nn);
						nn++;
					}
                    //change data to the right state and time
					if (rs.getString(6).equals("NY")) {
						next.countNY++;
						next.sumNY += rs.getInt(7);
					}

					if (rs.getString(6).equals("NJ")
							&& (rs.getInt(5) >= 1990 && rs.getInt(5) <= 1995)) {
						next.countNJ++;
						next.sumNJ += rs.getInt(7);

					}
					if (rs.getString(6).equals("CT")
							&& (rs.getInt(5) >= 1990 && rs.getInt(5) <= 1995)) {
						next.countCT++;
						next.sumCT += rs.getInt(7);
					}

					next = head;
					nn = 0;
				}

				if (false == Presave) {// for the question1, return false means, current product and row don't exist in the list, create and add them to the list
					// and using horizontal list to store the all data according to the state and time
					if (rs.getString(6).equals("NY")) {
						Qr = new Question1();
						Qr.Cust = rs.getString(1);
						Qr.Prod = rs.getString(2);
						Qr.cust_prod = rs.getString(1) + rs.getString(2);
						Qr.countNY = 1;
						Qr.countNJ = 0;
						Qr.countCT = 0;
						Qr.sumNY = rs.getInt(7);
						Qr.sumNJ = 0;
						Qr.sumCT = 0;
						Q1.add(Qr);
						judge.add(Qr.cust_prod);
					}

					if (rs.getString(6).equals("NJ")
							&& (rs.getInt(5) >= 1990 && rs.getInt(5) <= 1995)) {
						Qr = new Question1();
						Qr.Cust = rs.getString(1);
						Qr.Prod = rs.getString(2);
						Qr.cust_prod = rs.getString(1) + rs.getString(2);
						Qr.countNY = 0;
						Qr.countNJ = 1;
						Qr.countCT = 0;
						Qr.sumNY = 0;
						Qr.sumNJ = rs.getInt(7);
						Qr.sumCT = 0;
						Q1.add(Qr);
						judge.add(Qr.cust_prod);
					}

					if (rs.getString(6).equals("CT")
							&& (rs.getInt(5) >= 1990 && rs.getInt(5) <= 1995)) {
						Qr = new Question1();
						Qr.Cust = rs.getString(1);
						Qr.Prod = rs.getString(2);
						Qr.cust_prod = rs.getString(1) + rs.getString(2);
						Qr.countNY = 0;
						Qr.countNJ = 0;
						Qr.countCT = 1;
						Qr.sumNY = 0;
						Qr.sumNJ = 0;
						Qr.sumCT = rs.getInt(7);
						Q1.add(Qr);
						judge.add(Qr.cust_prod);
					}

					if ((rs.getString(6).equals("CT") && (rs.getInt(5) < 1990 || rs
							.getInt(5) > 1995))
							|| (rs.getString(6).equals("NJ") && (rs.getInt(5) < 1990 || rs
									.getInt(5) > 1995))
							|| rs.getString(6).equals("PA")) {
						Qr = new Question1();
						Qr.Cust = rs.getString(1);
						Qr.Prod = rs.getString(2);
						Qr.cust_prod = rs.getString(1) + rs.getString(2);
						Qr.countNY = 0;
						Qr.countNJ = 0;
						Qr.countCT = 0;
						Qr.sumNY = 0;
						Qr.sumNJ = 0;
						Qr.sumCT = 0;
						Q1.add(Qr);
						judge.add(Qr.cust_prod);// add customer and product to this list in order to judge whether customer and product exists in the list
					}

				}

				if (true == Presave23) {//for the question2,3, if true, means current customer and product already exist in the list, move the pointer to the row where current customer and product are
					while (!next23.cust_prod.contentEquals(rs.getString(1)
							+ rs.getString(2))) {
						next23 = Q23.get(nn23);
						nn23++;
					}
              /*in the following codes, we don't care what time and what state, just compare and select maximum and minimum quantity, 
				 in the same time storing all corresponding day,month, year and state */
					if (rs.getInt(7) > next23.Max_Quant) {
						next23.Max_Quant = rs.getInt(7);
						next23.maxDay = rs.getInt(3);
						next23.maxMonth = rs.getInt(4);
						next23.maxYear = rs.getInt(5);
						next23.maxState = rs.getString(6);
					}
					if (rs.getInt(7) < next23.Min_Quant) {
						next23.Min_Quant = rs.getInt(7);
						next23.minDay = rs.getInt(3);
						next23.minMonth = rs.getInt(4);
						next23.minYear = rs.getInt(5);
						next23.minState = rs.getString(6);
					}
					next23.allcount++;
					next23.allSum += rs.getInt(7);
					next23 = head23;
					nn23 = 0;
				}

				if (false == Presave23) {// for the question2,3, return false means, the current product and row don't exist in the list, create and add them to the vertical list
					/* and using horizontal list to store the all data according to the state, just assuming current state has maximum and minimum quantity, it may change 
					when compare to the data in next row if the quantity in next row is bigger or smaller than current maximum and minimum quantity */
					if (rs.getString(6).equals("NY")) {
						Qr23 = new Question1();
						Qr23.Cust = rs.getString(1);
						Qr23.Prod = rs.getString(2);
						Qr23.cust_prod = rs.getString(1) + rs.getString(2);
						Qr23.Max_Quant = rs.getInt(7);
						Qr23.Min_Quant = rs.getInt(7);
						Qr23.maxDay = rs.getInt(3);
						Qr23.maxMonth = rs.getInt(4);
						Qr23.maxYear = rs.getInt(5);
						Qr23.minDay = rs.getInt(3);
						Qr23.minMonth = rs.getInt(4);
						Qr23.minYear = rs.getInt(5);
						Qr23.maxState = "NY";
						Qr23.minState = "NY";
						Qr23.allcount = 1;
						Qr23.allSum = rs.getInt(7);
						Q23.add(Qr23);
						judge23.add(Qr23.cust_prod);// add customer and product to this list in order to judge whether customer and product exists in vertical list
					}

					if (rs.getString(6).equals("NJ")) {
						Qr23 = new Question1();
						Qr23.Cust = rs.getString(1);
						Qr23.Prod = rs.getString(2);
						Qr23.cust_prod = rs.getString(1) + rs.getString(2);
						Qr23.Max_Quant = rs.getInt(7);
						Qr23.Min_Quant = rs.getInt(7);
						Qr23.maxDay = rs.getInt(3);
						Qr23.maxMonth = rs.getInt(4);
						Qr23.maxYear = rs.getInt(5);
						Qr23.minDay = rs.getInt(3);
						Qr23.minMonth = rs.getInt(4);
						Qr23.minYear = rs.getInt(5);
						Qr23.maxState = "NJ";
						Qr23.minState = "NJ";
						Qr23.allcount = 1;
						Qr23.allSum = rs.getInt(7);
						Q23.add(Qr23);
						judge23.add(Qr23.cust_prod);
					}

					if (rs.getString(6).equals("CT")) {
						Qr23 = new Question1();
						Qr23.Cust = rs.getString(1);
						Qr23.Prod = rs.getString(2);
						Qr23.cust_prod = rs.getString(1) + rs.getString(2);
						Qr23.Max_Quant = rs.getInt(7);
						Qr23.Min_Quant = rs.getInt(7);
						Qr23.maxDay = rs.getInt(3);
						Qr23.maxMonth = rs.getInt(4);
						Qr23.maxYear = rs.getInt(5);
						Qr23.minDay = rs.getInt(3);
						Qr23.minMonth = rs.getInt(4);
						Qr23.minYear = rs.getInt(5);
						Qr23.maxState = "CT";
						Qr23.minState = "CT";
						Qr23.allcount = 1;
						Qr23.allSum = rs.getInt(7);
						Q23.add(Qr23);
						judge23.add(Qr23.cust_prod);
					}
					
					if (rs.getString(6).equals("PA")) {
						Qr23 = new Question1();
						Qr23.Cust = rs.getString(1);
						Qr23.Prod = rs.getString(2);
						Qr23.cust_prod = rs.getString(1) + rs.getString(2);
						Qr23.Max_Quant = rs.getInt(7);
						Qr23.Min_Quant = rs.getInt(7);
						Qr23.maxDay = rs.getInt(3);
						Qr23.maxMonth = rs.getInt(4);
						Qr23.maxYear = rs.getInt(5);
						Qr23.minDay = rs.getInt(3);
						Qr23.minMonth = rs.getInt(4);
						Qr23.minYear = rs.getInt(5);
						Qr23.maxState = "PA";
						Qr23.minState = "PA";
						Qr23.allcount = 1;
						Qr23.allSum = rs.getInt(7);
						Q23.add(Qr23);
						judge23.add(Qr23.cust_prod);
					}

				}
			}
			int nex = 0;
			int aveNJ, aveNY, aveCT;
			//print the question 1 out 
			System.out.println(String.format("%-10s", "CUSTOMER")
					+ String.format("%-9s", "PRODUCT")
					+ String.format("%-8s", "NY_AVG")
					+ String.format("%-8s", "NJ_AVG")
					+ String.format("%-8s", "CT_AVG"));
			System.out.println("========" + "  " + "=======" + "  " + "======"
					+ "  " + "======" + "  " + "======");
			while (nex < Q1.size()) {
				if (Q1.get(nex).countNY == 0)
					aveNY = 0;
				else
					aveNY = Q1.get(nex).sumNY / Q1.get(nex).countNY;

				if (Q1.get(nex).countNJ == 0)
					aveNJ = 0;
				else
					aveNJ = Q1.get(nex).sumNJ / Q1.get(nex).countNJ;

				if (Q1.get(nex).countCT == 0)
					aveCT = 0;
				else
					aveCT = Q1.get(nex).sumCT / Q1.get(nex).countCT;

				System.out.println(String.format("%-10s", Q1.get(nex).Cust)
						+ String.format("%-9s", Q1.get(nex).Prod)
						+ String.format("%-8d", aveNY)
						+ String.format("%-8d", aveNJ)
						+ String.format("%-8d", aveCT));
				nex++;

			}
			System.out.println();
			System.out.println();
			//print the question 1 out 
			//using string.formet to satisfy the requirement
			System.out.println(String.format("%-10s", "CUSTOMER")
					+ String.format("%-9s", "PRODUCT")
					+ String.format("%-7s", "MAX_Q")
					+ String.format("%-12s", "DATE")
					+ String.format("%-4s", "ST")
					+ String.format("%-7s", "MIN_Q")
					+ String.format("%-12s", "DATE")
					+ String.format("%-4s", "ST")
					+ String.format("%-5s", "AVG_Q"));
			System.out.println(String.format("%-10s", "========")
					+ String.format("%-9s", "=======")
					+ String.format("%-7s", "=====")
					+ String.format("%-12s", "==========")
					+ String.format("%-4s", "==")
					+ String.format("%-7s", "=====")
					+ String.format("%-12s", "==========")
					+ String.format("%-4s", "==")
					+ String.format("%-5s", "====="));
			nex = 0;
			int allAve;
			while (nex < Q23.size()) {
				if (Q23.get(nex).allcount == 0)
					allAve = 0;
				else
					allAve = Q23.get(nex).allSum / Q23.get(nex).allcount;
				
                 //skip state=="PA", print List Q23 out
				if (!(Q23.get(nex).maxState.equals("PA") || Q23.get(nex).minState
						.equals("PA"))) {

					System.out
							.println(String.format("%-10s", Q23.get(nex).Cust)
									+ String.format("%-9s", Q23.get(nex).Prod)
									+ String.format("%-7d",
											Q23.get(nex).Max_Quant)
									+ String.format("%02d",
											Q23.get(nex).maxMonth)
									+ "/"
									+ String.format("%02d", Q23.get(nex).maxDay)
									+ "/"
									+ String.format("%04d",
											Q23.get(nex).maxYear)
									+ "  "
									+ String.format("%-4s",
											Q23.get(nex).maxState)
									+ String.format("%-7d",
											Q23.get(nex).Min_Quant)
									+ String.format("%02d",
											Q23.get(nex).minMonth)
									+ "/"
									+ String.format("%02d", Q23.get(nex).minDay)
									+ "/"
									+ String.format("%04d",
											Q23.get(nex).minYear)
									+ "  "
									+ String.format("%-4s",
											Q23.get(nex).minState)
									+ String.format("%-5d", allAve));
				}
				nex++;
			}
		}

		catch (SQLException e) {
			System.out
					.println("Connection URL or username or password errors!");
			e.printStackTrace();
		}

	}

}
