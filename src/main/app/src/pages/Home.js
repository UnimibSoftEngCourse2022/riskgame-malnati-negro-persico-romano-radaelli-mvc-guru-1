import React from "react";
import { withAuth } from "../auth/AuthContext";
import LoginOrSignup from "../component/LoginOrSignup";
import SignUp from "../component/SignUp";
import Login from "../component/Login";
import PlayComponent from "../component/PlayComponent";

class Home extends React.Component {
  constructor() {
    super();
    this.state = {
      renderLogin: false,
      renderSignup: false,
    };
  }

  handleLogin = () => {
    this.setState({ renderLogin: true, renderSignup: false });
  };

  handleSignup = () => {
    this.setState({ renderLogin: false, renderSignup: true });
  };

  dismissComponent = () => {
    this.setState({ renderLogin: false, renderSignup: false });
  };

  render() {
    const { user } = this.props;
    console.log("user in home", user);

    return (
      <div id="Risiko">
        {!user.user && !this.state.renderLogin && !this.state.renderSignup && (
          <LoginOrSignup
            loginHandle={this.handleLogin}
            signupHandler={this.handleSignup}
          />
        )}
        {!user.user && this.state.renderLogin && (
          <Login onDismiss={this.dismissComponent} />
        )}
        {!user.user && this.state.renderSignup && (
          <SignUp
            onSuccess={this.handleLogin}
            onDismiss={this.dismissComponent}
          />
        )}
        {user.user && <span>Benvenuto {user.user}</span>}
        <h1 className="h1">Prova il nuovo Gioco di Risiko colions</h1>
        <PlayComponent />
      </div>
    );
  }
}
export default withAuth(Home);
