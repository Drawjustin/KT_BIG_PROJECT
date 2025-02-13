import { Clock, GitPullRequest, MessagesSquare, Zap } from 'lucide-react';
import styles from './About.module.css';

export default function About() {
 const features = [
   {
     icon: <Zap size={24} />,
     title: "AI 기반 분석",
     description: "최신 AI 기술을 활용하여 민원 내용을 정확하게 분석하고 분류합니다."
   },
   {
     icon: <GitPullRequest size={24} />,
     title: "자동 배정 시스템", 
     description: "분석된 내용을 바탕으로 가장 적합한 담당 부서에 자동으로 전달됩니다."
   },
   {
     icon: <Clock size={24} />,
     title: "신속한 처리",
     description: "접수된 민원은 24시간 이내에 담당자 검토가 이루어집니다."
   },
   {
     icon: <MessagesSquare size={24} />,
     title: "실시간 소통",
     description: "처리 과정을 실시간으로 확인하고 담당자와 직접 소통할 수 있습니다."
   }
 ];

 return (
   <div className={styles.container}>
     <div className={styles.complaintBoard}>
       <h2 className={styles.complaintTitle}>
         민원 게시판 이용 안내
       </h2>
       
       <p className={styles.description}>
         국민신문고는 여러분의 소중한 민원을 신속하고 정확하게 처리하기 위해 
         <span className={styles.highlight}>
           공무원SOS
         </span> 
         서비스를 제공합니다. 여러분이 작성한 민원 내용은 AI 모델을 통해 분석되며, 
         가장 적합한 담당 부서로 자동 전달됩니다. 더 효율적이고 원활한 민원 처리를 위해 최선을 다하겠습니다.
       </p>
       
       <div className={styles.featuresGrid}>
         {features.map((feature, index) => (
           <div key={index} className={styles.featureCard}>
             <div className={styles.iconWrapper}>
               {feature.icon}
             </div>
             <div className={styles.featureContent}>
               <h3 className={styles.featureTitle}>
                 {feature.title}
               </h3>
               <p className={styles.featureDescription}>
                 {feature.description}
               </p>
             </div>
           </div>
         ))}
       </div>
     </div>
   </div>
 );
}