import {jwtDecode} from "jwt-decode";

export function getUserRole(){
    const token = localStorage.getItem('token');

    if (!token){
        return null
    }

    try {
        const decodedToken = jwtDecode(token);

        const authorities = decodedToken.authorities;

        if (!authorities || authorities.length === 0) {
            return null;
        }

        return authorities[0];

    } catch (error) {
        console.error("JWT decoding error:", error);
        return null;
    }
}

export default  getUserRole();