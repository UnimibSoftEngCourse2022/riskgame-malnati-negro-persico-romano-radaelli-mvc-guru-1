import React, { createContext, useContext, useState } from "react";

const AuthContext = createContext();

export function useAuth() {
  return useContext(AuthContext);
}

export function withAuth(Component) {
  return function AuthComponent(props) {
    const user = useAuth();
    return <Component {...props} user={user} />;
  };
}

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loginStatus, setLoginStatus] = useState("");
  const [signUpStatus, setSignUpStatus] = useState("");

  const login = (username, password) => {
    fetch("/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        username: username,
        password: password,
      }),
    })
      .then((res) => {
        if (res.status === 200) {
          setUser(username);
          setLoginStatus("Login riuscito! Benvenuto.");
          console.log(loginStatus, username);
          return res.text();
        }
        throw new Error("Impossibile creare una partita");
      })
      .catch((error) => {
        console.error("Error:", error);
        setLoginStatus("Login fallito. Per favore, riprova.");
      });
  };

  const signUp = (username, password) => {
    fetch("/register", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        username: username,
        password: password,
      }),
    })
      .then((res) => {
        if (res.status === 200) {
          setSignUpStatus("Registrazione riuscita con successo!");
          return res.text();
        }
        throw new Error("Impossibile registrare l'utente");
      })
      .catch((error) => {
        console.error("Error:", error);
        setSignUpStatus(
          "Tentativo registrazione fallito. Per favore, riprova."
        );
      });
  };

  return (
    <AuthContext.Provider
      value={{ user, login, loginStatus, signUp, signUpStatus }}
    >
      {children}
    </AuthContext.Provider>
  );
};
