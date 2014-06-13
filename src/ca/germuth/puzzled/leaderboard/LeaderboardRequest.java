package ca.germuth.puzzled.leaderboard;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import net.sourceforge.jtds.jdbc.Driver;
import android.os.AsyncTask;
import android.util.Log;

public class LeaderboardRequest extends AsyncTask<Void, Void, Void> {
	private ArrayList<Solve> solves;

	public LeaderboardRequest() {

	}

	@Override
	protected Void doInBackground(Void... params) {

		ConnectToDatabase();

		return null;
	}

	public void ConnectToDatabase() {
		try {
			Driver.class.newInstance();

			Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
			String username = "PuzzledAndroidApp";
			String password = "puzzle";
			Connection DbConn = DriverManager

					.getConnection("jdbc:jtds:sqlserver://puzzleddatabase.cnessmlzxvbo.us-west-2.rds.amazonaws.com:1433;DatabaseName=Puzzled;user="
							+ username + ";password=" + password);

			Log.w("Connection", "open");
			Statement stmt = DbConn.createStatement();
			ResultSet reset = stmt.executeQuery(" SELECT * FROM Puzzle");

			String s = null;
			while (reset.next()) {
				s = reset.getString("name");
			}

			DbConn.close();

		} catch (Exception e) {
			Log.w("Error connection", "" + e.getMessage());
		}
	}

	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}

	public class Solve {
		private String user;
		private int duration;

		public Solve(String u, int d) {
			this.user = u;
			this.duration = d;
		}

		public String getUser() {
			return user;
		}

		public void setUser(String user) {
			this.user = user;
		}

		public int getDuration() {
			return duration;
		}

		public void setDuration(int duration) {
			this.duration = duration;
		}
	}
}

/*
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * package hello
 * 
 * import ( "html/template" "net/http" //"time" "strconv"
 * 
 * "appengine" "appengine/datastore" //"appengine/user" )
 * 
 * type Solve struct { Puzzle string Duration int User string }
 * 
 * func init() { http.HandleFunc("/", root) http.HandleFunc("/sign", sign) }
 * 
 * // guestbookKey returns the key used for all guestbook entries. func
 * guestbookKey(c appengine.Context) *datastore.Key { // The string
 * "default_guestbook" here could be varied to have multiple guestbooks. return
 * datastore.NewKey(c, "Solve", "default_solves", 0, nil) }
 * 
 * func root(w http.ResponseWriter, r *http.Request) { c :=
 * appengine.NewContext(r) // Ancestor queries, as shown here, are strongly
 * consistent with the High // Replication Datastore. Queries that span entity
 * groups are eventually // consistent. If we omitted the .Ancestor from this
 * query there would be // a slight chance that Greeting that had just been
 * written would not // show up in a query. q :=
 * datastore.NewQuery("Solve").Ancestor
 * (guestbookKey(c)).Order("-Duration").Limit(10) //greetings :=
 * make([]Greeting, 0, 10) solves := make([]Solve, 0, 10) if _, err :=
 * q.GetAll(c, &solves); err != nil { http.Error(w, err.Error(),
 * http.StatusInternalServerError) return } if err :=
 * guestbookTemplate.Execute(w, solves); err != nil { http.Error(w, err.Error(),
 * http.StatusInternalServerError) } }
 * 
 * var guestbookTemplate = template.Must(template.New("book").Parse(` <html>
 * <head> <title>Go Guestbook</title> </head> <body> {{range .}} {{with .User}}
 * <p><b>{{.}}</b> wrote:</p> {{else}} <p>An anonymous person wrote:</p> {{end}}
 * <pre>{{.Duration}}</pre> {{end}} <form action="/sign" method="post">
 * <div><textarea name="content" rows="3" cols="60"></textarea></div>
 * <div><input type="submit" value="Sign Guestbook"></div> </form> </body>
 * </html> `))
 * 
 * func sign(w http.ResponseWriter, r *http.Request) { c :=
 * appengine.NewContext(r) i,_ := strconv.ParseInt(r.FormValue("content"), 10,
 * 64); //i,_ := strconv.ParseInt(s, 10, 64); solve := Solve{ "3x3", int(i),
 * "aaron" }; // We set the same parent key on every Greeting entity to ensure
 * each Greeting // is in the same entity group. Queries across the single
 * entity group // will be consistent. However, the write rate to a single
 * entity group // should be limited to ~1/second. key :=
 * datastore.NewIncompleteKey(c, "Solve", guestbookKey(c)) _, err :=
 * datastore.Put(c, key, &solve) //g) if err != nil { http.Error(w, err.Error(),
 * http.StatusInternalServerError) return } http.Redirect(w, r, "/",
 * http.StatusFound) }
 */
