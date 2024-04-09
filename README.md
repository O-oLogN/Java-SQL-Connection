<h1>Course Registration Form</h1>
<h2>Features</h2>
<li>Providing form for students demanding in regsitering courses</li>
<li>Editable form by numerous functions: <code>Add</code>, <code>Delete</code> courses, <code>Cancel</code> regsister</li>
<li><code>Frontend</code> Powered by Java Swing, which brings a classic and effortless experience for users</li>
<li><code>Backend</code> Integrating SQL Server as a data storage for saving and retrieving all relevant data</li>
<br>
<h1>Frontend</h1>
<h2>Login site</h2>
<img src="https://github.com/O-oLogN/Java-SQL-Connection/assets/100874484/c13997d7-ee68-4aaa-9037-478990bc41e2" alt="login-ui">
<br>
<p>An authentication site will be displayed when you start the project, like other login forms, it requiers user to provide the correct <code>username</code> and <code>password</code>, the mechanism behind the scene is pretty simple<br></p> 
<li>Firstly, when rendering UI for user, a retrieving usernames & passwords process will be launched, and the results will be saved in a seperated container</li>
<li>Next, user should enter the correct username & password, there will have an alert for missing one of these informations, and user will pass this step if a pair of username and corresponding password matches with a provided one</li>
<br>
<h2>Registration form</h2>
<img src="https://github.com/O-oLogN/Java-SQL-Connection/assets/100874484/9ad21831-247b-4d2c-9c70-1f2213973554" alt="form-ui">
<br>
<p>A classic UI displayed in front of your eyesight, please feel free when witenssing its simplicity, and it can be divided into 2 main sections </p>
<h3><code>Section 1</code> Filling informations</h3>
<p>Let me introduce some basic traits</p>
<li>Maximizing user expirience by well-prepared lists which are contained in dropboxes, you can effortlessly choose your appropriate courses and relevant informations about the course will be automatically displayed in non-dropbox containers</li>
<li>Functional buttons: <code>Register</code> for verifying that you have filled up all course informations, and they will be exhibited on the table below,  <code>Remove</code> for deleting the chosen course in the table below as well as permanently removing your registered course, and <code>Save</code> for officialy saving courses into databases, which means you cannot modify your registered courses list unless you re-display them in the table on the following login session </li>
<br>
<h3><code>Section 2</code> Displaying registered courses</h3>
<p>As I mentioned above, this table will show you all courses that have been registered so far, including the past and after the moment you press a <code>Register</code>button</p>
<br>
<h1>Screenshots</h1>
<img align="center" src="https://github.com/O-oLogN/Java-SQL-Connection/assets/100874484/ee7fb01c-1c68-4143-a000-131299703a69" alt="login-failed">
<p><i>Login failed due to <code>Unidentified username</code> or <code>Wrong password</code></i></p>
<br>
<br>
<img src="https://github.com/O-oLogN/Java-SQL-Connection/assets/100874484/118e09bb-a440-4093-a710-3aa0637d4d7e" alt="register-course-1">
<p align="center"><i>Available courses list</i></p>
<br>
<br>
<img src="https://github.com/O-oLogN/Java-SQL-Connection/assets/100874484/73637cb6-842a-48d6-8912-1e4136fb1913" alt="register-course-2">
<p align="center"><i>Registering a course</i></p>
