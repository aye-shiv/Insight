package com.trivtech.insight.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.trivtech.insight.R;
import com.trivtech.insight.adapters.DeveloperListAdapter;
import com.trivtech.insight.adapters.DeveloperListAdapter.Developer;
import com.trivtech.insight.adapters.FeatureListAdapter;
import com.trivtech.insight.adapters.FeatureListAdapter.Feature;
import com.trivtech.insight.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;

public class About extends Fragment {

    TextView info;
    RecyclerView feature_list;
    RecyclerView developer_list;

    public About() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        info = view.findViewById(R.id.infotext);
        feature_list = view.findViewById(R.id.features_list);
        developer_list = view.findViewById(R.id.developers_list);

        updateLayout();
        init();
        return view;
    }

    public void updateLayout(){

        feature_list.getLayoutParams().width = Utils.getScreenWidthPercent(.95);

        developer_list.getLayoutParams().width = Utils.getScreenWidthPercent(.95);
    }

    public void init(){

        String infoText = "";
        infoText += "Project InSight addresses one of the main issues faced by the hearing impaired ";
        infoText += "community; which is that there exists a large majority of the population who ";
        infoText += "cannot understand sign language, and this puts a strain on their ability to ";
        infoText += "communicate with others. Thus, within this application one will be able to detect ";
        infoText += "and translate sign language in real time.\n";
        info.setText(infoText);

        Feature feature;
        ArrayList<Feature> features = new ArrayList<>();

        feature = new Feature();
        feature.name = "Gesture Editing";
        feature.description = "The main feature of this application where you translate the signs ";
        feature.description += "and make words or phrases out of them.\n";
        feature.description += "\n\n";

        feature.description += "~Gesture Commands~\n\n";

        feature.description += "Tap on translated bubble: \nAppends to message bubble the character displayed in the translated bubble\n\n";
        feature.description += "Tap on middle animated icon: \nAppends to message bubble the character displayed in the translated bubble\n\n";
        feature.description += "Swipe up on message bubble: \nRemoves last appended character in the message bubble\n\n";
        feature.description += "Swipe down on message bubble: \nRemoves all characters in the message bubble\n\n";

        feature.description += "\n\n";
        feature.description += "Try gesture editing on the translate page\n\n";
        feature.description += "[NOTE]: The message bubble only appears when you append your first character\n\n";

        feature.description += "[NOTE]: To add a space, press the middle animating icon when there is nothing in the translate bubble\n\n";
        features.add(feature);

        feature = new Feature();
        feature.name = "Text-to-Speech";
        feature.description = "Text-to-Speech or TTS enables the app to speak to you. \n";
        feature.description += "\n\n";

        feature.description += "When the app translates a sign for you the text is shown ";
        feature.description += "in the translate bubble above. When Text-To-Speech is enabled, after a sign is translated, ";
        feature.description += "you can click the middle icon to append characters which the TTS system will call out the text in an audio format. \n";

        feature.description += "\n\n";
        feature.description += "Enable/Disable TTS by pressing the speaker icon on the translate page. \n";
        features.add(feature);

        feature = new Feature();
        feature.name = "Cameras";
        feature.description += "In times when phones have multiple cameras, front and back it was necessary";
        feature.description += "to have support for those cameras.\n";
        feature.description = "The app supports realtime tracking for both front and back cameras. \n";

        feature.description += "\n\n";
        feature.description += "Switch between front and back cameras using the camera icon on the translate page. \n";
        features.add(feature);

        feature = new Feature();
        feature.name = "Dictionary";
        feature.description = "";
        feature.description += "For those who aren't familiar with the American Sign Language (ASL), No worries.";
        feature.description += "We have provided a dictionary with a list of ASL characters [A - Z].\n";

        feature.description += "\n\n";
        feature.description += "Visit Dictionary on the Home Page\n";
        feature.description += "[Click the 'View Dictionary' Button]\n";
        features.add(feature);

        feature_list.setAdapter(new FeatureListAdapter(features));

        //=========

        Developer developer;
        ArrayList<Developer> developers = new ArrayList<>();

        developer = new Developer();
        developer.name = "Shivam Persad";
        developer.id = "816016854";
        developer.role = "Leader";
        developers.add(developer);

        developer = new Developer();
        developer.name = "Brandon Noray";
        developer.id = "816018360";
        developer.role = "Designer";
        developers.add(developer);

        developer = new Developer();
        developer.name = "Maria Cardogan";
        developer.id = "816013573";
        developer.role = "Documentation Specialist";
        developers.add(developer);

        developer_list.setAdapter(new DeveloperListAdapter(developers));
    }

}