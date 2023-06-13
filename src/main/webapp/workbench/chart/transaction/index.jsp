<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <title>Title</title>

    <script type="text/javascript" src="ECharts/echarts.min.js"></script>
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>

    <script type="text/javascript">

        $(function (){

            getECharts();

        })

        function getECharts(){

            $.ajax({
                url:"workbench/transaction/getECharts.do",
                type:"get",
                dataType:"json",
                success:function(data){
                    /*
                    data
                        {"total":条数,"dataList":[{value: 60, name: 'Visit'}{value: 60, name: 'Visit'}{...}]}
                     */
                    // 基于准备好的dom，初始化echarts实例
                    var myChart = echarts.init(document.getElementById('main'));

                    // 指定图表的配置项和数据
                    var option = {
                        title: {
                            text: '交易漏斗图',
                            subtext:'统计交易数量的漏斗图'
                        },
                        toolbox: {
                            feature: {
                                dataView: { readOnly: false },
                                restore: {},
                                saveAsImage: {}
                            }
                        },
                        legend: {
                            data:data.dataList
                                // ['Show123', 'Click', 'Visit', 'Inquiry', 'Order','123']
                        },
                        series: [
                            {
                                name: '阶段统计',
                                type: 'funnel',
                                left: '10%',
                                top: 60,
                                bottom: 60,
                                width: '80%',
                                min: 0,
                                max: data.total,
                                minSize: '0%',
                                maxSize: '100%',
                                sort: 'descending',
                                gap: 2,
                                label: {
                                    show: true,
                                    position: ''
                                },
                                labelLine: {
                                    length: 10,
                                    lineStyle: {
                                        width: 1,
                                        type: 'solid'
                                    }
                                },
                                itemStyle: {
                                    borderColor: '#fff',
                                    borderWidth: 1
                                },
                                emphasis: {
                                    label: {
                                        fontSize: 20
                                    }
                                },
                                data: data.dataList
                                // [
                                //     { value: 60, name: 'Visit' },
                                //     { value: 40, name: 'Inquiry' },
                                //     { value: 20, name: 'Order' },
                                //     { value: 80, name: 'Click' },
                                //     { value: 100, name: 'Show123' }
                                // ]
                            }
                        ]
                    };

                    // 使用刚指定的配置项和数据显示图表。
                    myChart.setOption(option)
                }
            })

        }

    </script>
</head>
<body>
    <!-- 为 ECharts 准备一个定义了宽高的 DOM -->
    <div id="main" style="width: 1000px;height:500px;"></div>
</body>
</html>

