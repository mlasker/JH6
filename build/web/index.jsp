<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
    <h1>Person Collection</h1>
        
        <form method="post" action="peopleServlet" name="myForm">
            <input type="text" name="name"> Name</input><br>
            <input type="text" name="hairColor"> Hair Color</input><br>
            <input type="text" name="eyeColor"> Eye Color</input><br>
            <input type="text" name="height"> Height</input><br>
            <input type="text" name="weight"> Weight</input><br>
            <input type="submit" name="submit" value="Add Person">
            <input type="submit" name="submit" value="Create Table">
            <input type="submit" name="submit" value="Drop Table">
            <input type="submit" name="submit" value="CREATE TEST TABLE">
        </form><br>
   
    <table>
            <tr>
                <td>Name</td>
                <td>Hair Color</td>
                <td>Eye Color</td>
                <td>Height</td>
                <td>Weight</td>
            </tr>
            <c:forEach var="poop" items="${pc}">
            <tr>
                <form method="post" action="peopleServlet">
                <td><input type="text" name="name" value="${poop.name}"></input></td>
                <td><input type="text" name="hairColor" value="${poop.hairColor}"</input></td>
                <td><input type="text" name="eyeColor" value="${poop.eyeColor}"</input></td>
                <td><input type="text" name="height" value="${poop.height}"</input></td>
                <td><input type="text" name="weight" value="${poop.weight}"</input></td>
                <td><input type="submit" value="Delete" name="submit"> </td>
                <td><input type="submit" value="Edit" name="submit"> </td>
                <td><input type="hidden" value="${poop.id}" name="id"></td>
                </form>
            </c:forEach>
            </tr>
        </table>
</html>
