import axios from 'axios';
import React from 'react';
import { useState, useEffect, useId } from 'react';
import { Link, useParams } from "react-router-dom";

export default function ControlPanel() {
    const { deviceId } = useParams();
    const [lampState, setLampState] = useState({power: true, brightness:10, effect: 'Auto'});
    const [ledState, setLedState] = useState({color1: '#ffffff', color2: '#000000', color3: '#000000', effect: 0, palette: 0, speed: 128, intensity: 128});
    const [effects, setEffects] = useState([]);
    const [palettes, setPalettes] = useState([]);
    const [presets, setPresets] = useState([]);

    function updateData() {
        axios.get('data').then(({data}) => {
            if (data.presets) {
                setPresets(data.presets);
            }
            if (data.lamp) {
                setLampState(data.lamp);
            }
            if (data.style) {
                setLedState(data.style);
            }
        });
    }

    useEffect(() => {
        console.log("Load data for: ", deviceId);
        axios.get('effects').then(({data}) => { setEffects(data.sort((a, b) => a.name.localeCompare(b.name)))});
        axios.get('palettes').then(({data}) => { setPalettes(data.sort((a, b) => a.name.localeCompare(b.name)))});
        // axios.get('state').then(({data}) => { setLedState(data); });
        updateData();
    }, [deviceId]);

    function changeLedState(state) {
        setLampState({...lampState, preset: 'Custom style'});
        setLedState(state);
        axios.post('state', state);
    }

    function changeLampState(state) {
        setLampState(state);
        axios.post('lamp', state).then(updateData);
    }

    console.info(ledState);

    const currentPreset = lampState.preset;
    const presetList = presets.map((preset) => {
        return <LampPreset
          key={preset.name}
          preset={preset.name}
          selected={preset.name == currentPreset}
          onSelect={(preset) => {changeLampState({...lampState, preset: preset })}}>{preset.name}</LampPreset>
    });
    const paletteList = palettes.map((palette) => {
        return <LedPalette
                key={palette.id}
                palette={palette}
                selected={palette.id == ledState.palette}
                onSelect={(paletteId) => {changeLedState({... ledState, palette: paletteId}); }} />
    });

    const effectList = effects.map((effect) => {
        return <LedEffect
            key={effect.id}
            effect={effect}
            selected={effect.id == ledState.effect}
            onSelect={(effectId) => {changeLedState({... ledState, effect: effectId}); }} />
    });

    return (
        <div className="led-control">
            <header>
                <h1>{deviceId}</h1>
                <input type="checkbox" id="power" checked={lampState.powerOn} onChange={(event) => {changeLampState({...lampState, powerOn: event.target.checked})}} />
                <input type="range" min="1" max="255" id="brightness" value={lampState.brightness} onChange={(event) => {changeLampState({...lampState, brightness: event.target.value, powerOn: true})}} />

            </header>
            <article>
                <section id="presets">
                    <div className="scrollable">
                        <div className="optionlist">
                            {presetList}
                        </div>
                    </div>
                </section>

                <section id="coloroptions">
                    <div id="colors">
                        <ColorPicker name="Color 1" color={ledState.color1} onChange={(color) => {changeLedState({...ledState, color1: color})}} />
                        <ColorPicker name="Color 2" color={ledState.color2} onChange={(color) => {changeLedState({...ledState, color2: color})}} />
                        <ColorPicker name="Color 3" color={ledState.color3} onChange={(color) => {changeLedState({...ledState, color3: color})}} />
                    </div>
                    <div className="scrollable">
                        <div className="palettes optionlist">
                            { paletteList }
                        </div>
                    </div>

                </section>

                <section id="effects">
                    <div id="animationOptions">
                        <div>
                            <label htmlFor="speed">Speed</label>
                            <input id="speed" type="range" min="0" max="255"
                                value={ledState.speed} onChange={(event) => { changeLedState({...ledState, speed: 1 * event.target.value}) }}
                                className="slider" />
                        </div>
                        <div>
                            <label htmlFor="intensity">Intensity</label>
                            <input id="intensity" type="range" min="0" max="255"
                                value={ledState.intensity} onChange={(event) => { changeLedState({...ledState, intensity: 1 * event.target.value}) }}
                                className="slider" />
                        </div>
                    </div>
                    <div className="scrollable">
                        <ul className="effects optionlist">{ effectList }</ul>
                    </div>
                </section>
            </article>
        </div>
    );
 
}

function createPalettePreview(palette) {
    const preview = 'linear-gradient(to right, '
      + palette.style.map((style) => {
          if (Array.isArray(style)) {
              return 'rgb(' + style[1] + ',' + style[2] + ',' + style[3] + ') ' + (style[0] * 100 / 255) + '%'
          }
          return '';
      })
      + ')';
      return preview;
}

function LedPalette({palette, selected, onSelect=function(){}}) {
    return (
        <ToggleButton
            className='palette'
            size="sm"
            type="checkbox"
            variant="outline-primary"
            checked={selected}
            value={palette.id}
            onClick={(e) => { onSelect(palette.id); }} >
            {palette.name}<div className='palette-preview' style={{ background: createPalettePreview(palette)}} />
        </ToggleButton>
    );
}

function LampPreset({preset, selected=false, onSelect=function(){}}) {
    return (
        <ToggleButton
            checked={selected}
            value={preset}
            onClick={(e) => { onSelect(preset); }}>
            {preset}
        </ToggleButton>
    );
}

function LedEffect({effect, selected=false, onSelect=function(){}}) {
    return (
        <ToggleButton
            checked={selected}
            value={effect.id}
            onClick={(e) => { onSelect(effect.id); }}>
            {effect.name}
        </ToggleButton>
    );
}

function ColorPicker({name, color='#000000', onChange=function(){} }) {
    const id = useId();
    function onColorSelected(event) {
        onChange(event.target.value);
    };

    return <div>
            <label htmlFor={id}>{name}</label>
            <input id={id} type="color" value={color} onChange={onColorSelected} />&nbsp;
        </div>
}

function ToggleButton({checked, value, onClick, children}) {
    let className = checked ? 'active' : null;
    return <button className={className} onClick={onClick}>{children}</button>;
}
