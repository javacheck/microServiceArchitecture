<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<m:table pageNo="${page.pageNo }" pages="${page.pages }"
	pageSize="${page.pageSize }" total="${page.total }" >
	<thead>
		<tr> 
			<th>序号</th>
			<c:if test="${isSys==true || isMainStore==true}">
			<th>商家名称</th>
			</c:if>
			<th>商品名称</th>	
			<th>商品条码</th>
			<th>规格</th>
			<th>单位</th>
			<th class="two">库存数量<span class="arrows" name="numAsc" value="1"></span><span class="arrows boult" name="numAsc" value="2"></span></th>
			<th class="two">缺货提醒<span class="arrows" name="numAsc" value="3"></span><span class="arrows boult" name="numAsc" value="4"></span></th>
			<th class="two">进货价<span class="arrows" name="numAsc" value="5"></span><span class="arrows boult" name="numAsc" value="6"></span></th>
			<th class="two">销售价<span class="arrows" name="numAsc" value="7"></span><span class="arrows boult" name="numAsc" value="8"></span></th>
			<th class="two">会员价<span class="arrows" name="numAsc" value="9"></span><span class="arrows boult" name="numAsc" value="10"></span></th>
			<th>商品状态</th>
			<th>商品分类</th>
			<th>品牌</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${page.data }" var="productStock" varStatus="var">
			<tr >
				<td>${var.count}</td>
				<c:if test="${isSys==true || isMainStore==true}">
				<td>${productStock.storeName }</td>
				</c:if>
				<td>${productStock.productName}</td>
				<td>${productStock.barCode}</td>
				<td>${productStock.attributeValues}</td>
				<td>${productStock.unitName}</td>
				<td>
					<c:if test="${productStock.stock <0}">
						无限
					</c:if>
					<c:if test="${productStock.stock >=0}">
						<c:if test="${productStock.stock <=productStock.alarmValue}">
							<span style="color:red;font-weight:bold">${productStock.stock}</span>
						</c:if>
						<c:if test="${productStock.stock >productStock.alarmValue}">
							${productStock.stock}
						</c:if>
					</c:if>
				</td>
				<td>
					<c:if test="${productStock.stock >=0}">
						<c:if test="${productStock.stock <=productStock.alarmValue}">
							<span style="color:red;font-weight:bold">${productStock.alarmValue}</span>
						</c:if>
						<c:if test="${productStock.stock >productStock.alarmValue}">
							${productStock.alarmValue}
						</c:if>
					</c:if>
					<c:if test="${productStock.stock <0}">
						${productStock.alarmValue}
					</c:if>
				</td>
				<td>￥<fmt:formatNumber value="${productStock.costPrice }"  type="currency" pattern="0.00"/></td>
				<td>￥<fmt:formatNumber value="${productStock.price }"  type="currency" pattern="0.00"/></td>
				<td>
					<c:if test="${productStock.memberPrice==null || productStock.memberPrice=='' }"></c:if>
					<c:if test="${productStock.memberPrice!=null && productStock.memberPrice!=''}">￥<fmt:formatNumber value="${productStock.memberPrice }"  type="currency" pattern="0.00"/></c:if>
				</td>
				<td>
					<c:if test="${productStock.shelves==0}">仅收银端上架</c:if>
					<c:if test="${productStock.shelves==2}">仅APP端上架</c:if>
					<c:if test="${productStock.shelves==4}">收银端、APP端均上架</c:if>
					<c:if test="${productStock.shelves==5}">全部下架</c:if>
				</td>
				<td>${productStock.categoryName}</td>
				<td>${productStock.brandName}</td>
			</tr>
		</c:forEach>
	</tbody>
</m:table>