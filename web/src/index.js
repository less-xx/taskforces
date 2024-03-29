import React from 'react';
import ReactDOM from 'react-dom';
import { createStore, applyMiddleware } from 'redux';
import thunk from 'redux-thunk';
import { Provider } from 'react-redux';
import './index.css';
import 'bootstrap/dist/css/bootstrap.css';

import * as serviceWorker from './serviceWorker';
import App from './app/App';
import rootReducer from './reducers';

const store = createStore(rootReducer, applyMiddleware(thunk));
//const unsubscribe = store.subscribe(() => console.log(store.getState()))
const Root = () => (
    <Provider store={store}>
        <App />
    </Provider>
)


ReactDOM.render(<Root />, document.getElementById('root'));


// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA

serviceWorker.unregister();
