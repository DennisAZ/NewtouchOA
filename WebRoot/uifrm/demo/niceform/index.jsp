<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/core/inc/t6.jsp" %>
<title>zTree</title>
<script type="text/javascript" src="../../js/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="../../js/ui/jquery-ui-1.8.17.custom.min.js"></script>
<script type="text/javascript" src="niceforms.js"></script>
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/style.css"/>
<link rel="stylesheet" type="text/css" href="niceforms-default.css"/>
<script type="text/javascript">
</script>
<style>

</style>
</head>
<body>

	<form action="vars.php" method="post" class="niceform">
	<div class="formctn">
      <fieldset class="right">
        <dl>

          <dt><label for="email">邮件地址:</label></dt>

            <dd><input type="text" name="email" id="email" size="32" maxlength="128" /></dd>

        </dl>

        <dl>

          <dt><label for="password">密码:</label></dt>

            <dd><input type="password" name="password" id="password" size="32" maxlength="32" /></dd>

        </dl>

        <dl>

          <dt><label for="gender">性别:</label></dt>

            <dd>

              <select size="1" name="gender" id="gender">

                    <option value="Guy">男</option>

                    <option value="Girl">女</option>

                    <option value="Don't Ask">不知道</option>

              </select>

            </dd>

        </dl>

        <dl>

          <dt><label for="dobMonth">出生日期:</label></dt>

            <dd>

              <select size="1" name="dobMonth" id="dobMonth">

                  <option value="Jan">一月</option>

                    <option value="Feb">二月</option>

                    <option value="Mar">Mar</option>

                    <option value="Apr">Apr</option>

                    <option value="May">May</option>

                    <option value="Jun">Jun</option>

                    <option value="Jul">Jul</option>

                    <option value="Aug">Aug</option>

                    <option value="Sep">Sep</option>

                    <option value="Oct">Oct</option>

                    <option value="Nov">Nov</option>

                    <option value="Dec">Dec</option>

                </select>

                <select size="1" name="dobDay" id="dobDay">

                  <option value="01">01</option>

                    <option value="02">02</option>

                    <option value="03">03</option>

                    <option value="04">04</option>

                    <option value="05">05</option>

                    <option value="06">06</option>

                    <option value="07">07</option>

                    <option value="08">08</option>

                    <option value="09">09</option>

                    <option value="10">10</option>

                    <option value="11">11</option>

                    <option value="12">12</option>

                    <option value="13">13</option>

                    <option value="14">14</option>

                    <option value="15">15</option>

                    <option value="16">16</option>

                    <option value="17">17</option>

                    <option value="18">18</option>

                    <option value="19">19</option>

                    <option value="20">20</option>

                    <option value="21">21</option>

                    <option value="22">22</option>

                    <option value="23">23</option>

                    <option value="24">24</option>

                    <option value="25">25</option>

                    <option value="26">26</option>

                    <option value="27">27</option>

                    <option value="28">28</option>

                    <option value="29">29</option>

                    <option value="30">30</option>

                    <option value="31">31</option>

                </select>

                <select size="1" name="dobYear" id="dobYear">

                  <option value="2000">2000</option>

                    <option value="1999">1999</option>

                    <option value="1998">1998</option>

                    <option value="1997">1997</option>

                    <option value="1996">1996</option>

                    <option value="1995">1995</option>

          <option value="1994">1994</option>

          <option value="1993">1993</option>

          <option value="1992">1992</option>

          <option value="1991">1991</option>

          <option value="1990">1990</option>

          <option value="1989">1989</option>

          <option value="1988">1988</option>

          <option value="1987">1987</option>

          <option value="1986">1986</option>

          <option value="1985">1985</option>

          <option value="1984">1984</option>

          <option value="1983">1983</option>

          <option value="1982">1982</option>

          <option value="1981">1981</option>

          <option value="1980">1980</option>

          <option value="1979">1979</option>

          <option value="1978">1978</option>

          <option value="1977">1977</option>

          <option value="1976">1976</option>

                </select>

            </dd>

        </dl>

        <dl>

          <dt><label for="color">喜爱的颜色:</label></dt>

            <dd>

              <input type="radio" name="color" id="colorBlue" value="Blue" /><label for="colorBlue" class="opt">蓝色</label>

                <input type="radio" name="color" id="colorRed" value="Red" /><label for="colorRed" class="opt">红色</label>

                <input type="radio" name="color" id="colorGreen" value="Green" /><label for="colorGreen" class="opt">绿色</label>

            </dd>

        </dl>

        <dl>

          <dt><label for="interests">兴趣:</label></dt>

            <dd>

                <input type="checkbox" name="interests[]" id="interestsNews" value="News" /><label for="interestsNews" class="opt">新闻</label>

                <input type="checkbox" name="interests[]" id="interestsSports" value="Sports" /><label for="interestsSports" class="opt">运动</label>

                <input type="checkbox" name="interests[]" id="interestsEntertainment" value="Entertainment" /><label for="interestsEntertainment" class="opt">娱乐</label>

                <input type="checkbox" name="interests[]" id="interestsCars" value="Cars" /><label for="interestsCars" class="opt">Automotive</label>

                <input type="checkbox" name="interests[]" id="interestsTechnology" value="Technology" /><label for="interestsTechnology" class="opt">Technology</label>

            </dd>

        </dl>

        <dl>

          <dt><label for="languages">语言:</label></dt>

            <dd>

              <select size="4" name="languages[]" id="languages" multiple="multiple">

                  <option value="English">English</option>

                    <option value="French">French</option>

                    <option value="Spanish">Spanish</option>

                    <option value="Italian">Italian</option>

                    <option value="Chinese">Chinese</option>

                    <option value="Japanese">Japanese</option>

                    <option value="Russian">Russian</option>

                    <option value="Esperanto">Esperanto</option>

                </select>

            </dd>

        </dl>

        <dl>

          <dt><label for="comments">Message:</label></dt>

            <dd><textarea name="comments" id="comments" rows="5" cols="60"></textarea></dd>

        </dl>

        <dl>

          <dt><label for="upload">Upload a File:</label></dt>

            <dd><input type="file" name="upload" id="upload" /></dd>

        </dl>

        <dl>

          <dt><label for="test">Sample Button:</label></dt>

            <dd><button type="button" name="test" id="test">I Do Nothing</button></dd>

        </dl>

    </fieldset>

    <fieldset class="action">

      <input type="submit" name="submit" id="submit" value="提交" />

    </fieldset>
</div>
</form>
</body>
</html>