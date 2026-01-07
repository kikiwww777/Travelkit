package com.example.check.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 敏感词过滤工具类
 * 用于检测和过滤政治敏感词和骂人词汇
 */
public class SensitiveWordFilter {

    private static final Set<String> SENSITIVE_WORDS = new HashSet<>(Arrays.asList(
        // 政治敏感词
        "法轮功", "法轮", "大法", "李洪志", "六四", "六四事件", "天安门", "天安门事件",
        "台独", "藏独", "疆独", "港独", "分裂", "独立", "反动", "反革命",
        "共产党", "中共", "政府", "国家领导人", "领导人", "主席", "总理",
        "政治", "政权", "专政", "民主", "自由", "人权", "言论自由",
        "抗议", "示威", "游行", "暴动", "革命", "起义", "政变",
        "恐怖", "恐怖主义", "恐怖分子", "炸弹", "爆炸", "袭击",
        "毒品", "吸毒", "贩毒", "制毒", "大麻", "海洛因", "冰毒",
        "赌博", "赌场", "赌局", "博彩", "彩票", "六合彩",
        "色情", "黄色", "成人", "性爱", "情色", "AV", "三级",
        "诈骗", "传销", "非法", "违法", "犯罪", "黑社会",
        
        // 中国领导人
        "习近平", "李克强", "胡锦涛", "温家宝", "江泽民", "朱镕基", "李鹏",
        "邓小平", "毛泽东", "周恩来", "刘少奇", "朱德", "陈云", "林彪",
        "华国锋", "叶剑英", "李先念", "汪东兴", "王洪文", "张春桥", "姚文元",
        "赵紫阳", "杨尚昆", "万里", "乔石", "李瑞环", "宋平", "刘华清",
        "尉健行", "李岚清", "黄菊", "吴官正", "李长春", "罗干", "贺国强",
        "周永康", "王岐山", "张高丽", "马凯", "刘延东", "汪洋", "张德江",
        "俞正声", "韩正", "王沪宁", "栗战书", "赵乐际", "丁薛祥", "李希",
        "王晨", "刘鹤", "许其亮", "孙春兰", "杨洁篪", "杨晓渡", "陈希",
        "黄坤明", "尤权", "肖捷", "张庆黎", "何立峰", "巴特尔", "苏辉",
        "辜胜阻", "刘新成", "何维", "邵鸿", "高云龙", "郑建邦", "万钢",
        "王光谦", "秦博勇", "朱永新", "张雨东", "王红", "张道宏", "李世杰",
        "张少康", "张恩迪", "李卓彬", "周汉民", "蔡达峰", "陈群", "丁仲礼",
        "郝明金", "蔡威", "蒋作君",
        
        // 骂人词汇
        "傻逼", "傻B", "SB", "煞笔", "沙比", "沙壁",
        "草泥马", "操你妈", "操你", "操", "艹",
        "妈的", "他妈的", "你妈的", "卧槽", "我靠",
        "滚", "滚蛋", "滚开", "去死", "死", "死全家",
        "白痴", "弱智", "智障", "脑残", "脑瘫",
        "垃圾", "废物", "人渣", "败类", "杂种",
        "贱", "贱人", "贱货", "婊子", "妓女",
        "王八", "王八蛋", "龟孙子", "狗", "狗日的",
        "日", "干", "艹", "肏",
        "你妹", "你大爷", "你妈", "你爸",
        "靠", "靠你", "靠你妈",
        "TMD", "TM", "NMD", "NM",
        "FUCK", "FUCK YOU", "SHIT", "ASS", "BITCH",
        "混蛋", "混账", "混球",
        "神经病", "精神病", "疯子", "变态",
        "去你", "去你妈", "去你大爷",
        "老子", "老娘", "你老子",
        "装逼", "装B", "装蒜", "装模作样",
        "傻X", "蠢货", "蠢猪", "蠢蛋",
        "二逼", "二B", "二货", "二百五",
        "土鳖", "乡巴佬", "穷鬼",
        "老不死", "老东西", "老家伙",
        "小屁孩", "小崽子", "小杂种",
        "死鬼", "死猪", "死狗",
        "滚犊子", "滚蛋", "滚开",
        "放屁", "屁话", "胡说",
        "扯淡", "扯犊子", "瞎说",
        "有病", "神经", "发神经",
        "欠揍", "欠打", "欠收拾",
        "找死", "作死", "想死",
        "活该", "报应", "倒霉",
        "倒霉蛋", "倒霉鬼", "扫把星",
        "扫兴", "败兴", "晦气",
        "晦气鬼", "丧门星", "灾星",
        "克星", "煞星", "灾星",
        "祸害", "祸水", "扫把",
        "扫帚星", "丧门", "丧门神"
    ));

    /**
     * 检测文本是否包含敏感词
     * @param text 要检测的文本
     * @return 如果包含敏感词返回true，否则返回false
     */
    public static boolean containsSensitiveWord(String text) {
        if (text == null || text.trim().isEmpty()) {
            return false;
        }
        
        String normalizedText = text.trim();
        
        // 对于中文敏感词，直接比较；对于英文敏感词，转换为小写比较
        for (String word : SENSITIVE_WORDS) {
            // 检查是否为英文敏感词（包含英文字母）
            boolean isEnglishWord = word.matches(".*[a-zA-Z].*");
            if (isEnglishWord) {
                // 英文敏感词：转换为小写比较
                if (normalizedText.toLowerCase().contains(word.toLowerCase())) {
                    return true;
                }
            } else {
                // 中文敏感词：直接比较
                if (normalizedText.contains(word)) {
                    return true;
                }
            }
        }
        
        return false;
    }

    /**
     * 检测文本并返回第一个匹配的敏感词
     * @param text 要检测的文本
     * @return 如果包含敏感词返回第一个匹配的词，否则返回null
     */
    public static String findSensitiveWord(String text) {
        if (text == null || text.trim().isEmpty()) {
            return null;
        }
        
        String normalizedText = text.trim();
        
        // 对于中文敏感词，直接比较；对于英文敏感词，转换为小写比较
        for (String word : SENSITIVE_WORDS) {
            // 检查是否为英文敏感词（包含英文字母）
            boolean isEnglishWord = word.matches(".*[a-zA-Z].*");
            if (isEnglishWord) {
                // 英文敏感词：转换为小写比较
                if (normalizedText.toLowerCase().contains(word.toLowerCase())) {
                    return word;
                }
            } else {
                // 中文敏感词：直接比较
                if (normalizedText.contains(word)) {
                    return word;
                }
            }
        }
        
        return null;
    }

    /**
     * 过滤敏感词（用*替换）
     * @param text 要过滤的文本
     * @return 过滤后的文本
     */
    public static String filterSensitiveWord(String text) {
        if (text == null || text.trim().isEmpty()) {
            return text;
        }
        
        String result = text;
        for (String word : SENSITIVE_WORDS) {
            result = result.replaceAll("(?i)" + word, "*".repeat(word.length()));
        }
        
        return result;
    }

    /**
     * 验证文本，如果包含敏感词则抛出异常
     * @param text 要验证的文本
     * @throws IllegalArgumentException 如果包含敏感词
     */
    public static void validateText(String text) {
        if (containsSensitiveWord(text)) {
            throw new IllegalArgumentException("请勿输入敏感内容");
        }
    }
}
