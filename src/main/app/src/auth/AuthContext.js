import React, { createContext, useContext, useState } from "react";

const AuthContext = createContext();

export function useAuth() {
  return useContext(AuthContext);
}

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loginStatus, setLoginStatus] = useState("");

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
        console.log("Login Fallito");
        throw new Error("Impossibile creare una partita");
      })
      .catch((error) => {
        console.error("Error:", error);
        setLoginStatus("Login fallito. Per favore, riprova.");
      });
  };

  return (
    <AuthContext.Provider value={{ user, login, loginStatus }}>
      {children}
    </AuthContext.Provider>
  );
};
